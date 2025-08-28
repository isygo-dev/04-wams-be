package eu.isygoit.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.isygoit.annotation.InjectCodeGen;
import eu.isygoit.annotation.InjectCodeGenKms;
import eu.isygoit.annotation.InjectRepository;
import eu.isygoit.com.rest.service.CodeAssignableService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.TenantConstants;
import eu.isygoit.dto.common.*;
import eu.isygoit.dto.data.*;
import eu.isygoit.dto.extendable.AccountModelDto;
import eu.isygoit.enums.IEnumEmailTemplate;
import eu.isygoit.enums.IEnumInterviewSkillType;
import eu.isygoit.enums.IEnumJobAppEventType;
import eu.isygoit.enums.IEnumPositionType;
import eu.isygoit.exception.*;
import eu.isygoit.mapper.InterviewSkillsMapper;
import eu.isygoit.model.*;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.cms.CmsCalendarEventService;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.*;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAndCodeAssignableRepository;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAssignableRepository;
import eu.isygoit.service.IMsgService;
import eu.isygoit.service.IResumeService;
import eu.isygoit.service.IWorkflowBoardService;
import eu.isygoit.types.EmailSubjects;
import eu.isygoit.types.MsgTemplateVariables;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional
@InjectCodeGen(value = NextCodeService.class)
@InjectCodeGenKms(value = KmsIncrementalKeyService.class)
@InjectRepository(value = WorkflowBoardRepository.class)
public class WorkflowBoardService extends CodeAssignableService<Long, WorkflowBoard, WorkflowBoardRepository> implements IWorkflowBoardService {

    private final AppProperties appProperties;

    private final WorkflowBoardRepository workflowBoardRepository;
    private final WorkflowRepository workflowRepository;
    private final JobOfferApplicationEventRepository jobApplicationEventRepository;
    private final InterviewEventRepository interviewEventRepository;
    private final JobOfferApplicationRepository jobApplicationRepository;
    private final InterviewSkillsMapper interviewSkillsMapper;
    private final GenericRepositoryService genericRepository;
    private final CmsCalendarEventService cmsCalendarEventService;
    private final IResumeService resumeService;

    private final IMsgService msgService;

    private final WorkflowStateRepository workflowStateRepository;

    @Autowired
    public WorkflowBoardService(AppProperties appProperties, WorkflowBoardRepository workflowBoardRepository, WorkflowRepository workflowRepository, JobOfferApplicationEventRepository jobApplicationEventRepository, InterviewEventRepository interviewEventRepository, JobOfferApplicationRepository jobApplicationRepository, InterviewSkillsMapper interviewSkillsMapper, GenericRepositoryService genericRepository, CmsCalendarEventService cmsCalendarEventService, IResumeService resumeService, IMsgService msgService, WorkflowStateRepository workflowStateRepository) {
        this.appProperties = appProperties;
        this.workflowBoardRepository = workflowBoardRepository;
        this.workflowRepository = workflowRepository;
        this.jobApplicationEventRepository = jobApplicationEventRepository;
        this.interviewEventRepository = interviewEventRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.interviewSkillsMapper = interviewSkillsMapper;
        this.genericRepository = genericRepository;
        this.cmsCalendarEventService = cmsCalendarEventService;
        this.resumeService = resumeService;
        this.msgService = msgService;
        this.workflowStateRepository = workflowStateRepository;
    }

    @Override
    public WorkflowBoard beforeCreate(WorkflowBoard workflowBoard) {
        workflowRepository.findByCodeIgnoreCase(workflowBoard.getWorkflow().getCode())
                .ifPresent(workflow -> workflowBoard.setWorkflow(workflow));
        return super.beforeCreate(workflowBoard);
    }

    @Override
    public WorkflowBoard beforeUpdate(WorkflowBoard workflowBoard) {
        workflowRepository.findByCodeIgnoreCase(workflowBoard.getWorkflow().getCode())
                .ifPresent(workflow -> workflowBoard.setWorkflow(workflow));
        return super.beforeUpdate(workflowBoard);
    }

    @Override
    public List<WorkflowState> getStates(String wbCode) {
        return this.workflowBoardRepository.findByCodeIgnoreCase(wbCode)
                .map(workflowBoard -> workflowBoard.getWorkflow().getWorkflowStates())
                .orElseThrow(() -> new WorkflowBoardNotFoundException("with code " + wbCode));
    }

    @Override
    public List<IBoardItem> getItems(String tenant, String wbCode) throws ClassNotFoundException {
        WorkflowBoard workflowBoard = this.workflowBoardRepository
                .findByCodeIgnoreCase(wbCode)
                .orElseThrow(() -> new WorkflowBoardNotFoundException("with code " + wbCode));

        Workflow workflow = Optional.ofNullable(workflowBoard.getWorkflow())
                .orElseThrow(() -> new WorkflowNotParametrizeException("wb code " + wbCode));

        List<WorkflowState> workflowStates = Optional.ofNullable(workflow.getWorkflowStates())
                .filter(ws -> !CollectionUtils.isEmpty(ws))  // using CollectionUtils.isEmpty
                .orElseThrow(() -> new WorkflowStatesNotParametrizeException("wf code " + workflow.getCode()));

        WorkflowState initialState = workflowStates.stream()
                .filter(ws -> ws.getPositionType().equals(IEnumPositionType.Types.INIT))
                .findFirst()
                .orElseGet(() -> workflowStates.get(0)); // Default to first state

        JpaPagingAndSortingTenantAssignableRepository repository =
                (JpaPagingAndSortingTenantAssignableRepository) genericRepository.getRepository(workflowBoard.getItem());

        return (List<IBoardItem>) repository.findByTenantIgnoreCaseIn(Collections.singletonList(tenant)).stream()
                .map(obj -> {
                    IBoardItem item = (IBoardItem) obj;
                    String state = (item.getState() != null && stateExists((String) item.getState()))
                            ? (String) item.getState()
                            : initialState.getCode();

                    List<MiniBoardEventDto> events = (List<MiniBoardEventDto>) item.getEvents().stream()
                            .filter(e -> isEventNonDeletedInCalendar(tenant, (IBoardEvent) e))
                            .map(e -> MiniBoardEventDto.builder()
                                    .id(((IBoardEvent) e).getId())
                                    .title(((IBoardEvent) e).getTitle())
                                    .type(((Enum<?>) ((IBoardEvent) e).getType()).name())
                                    .build())
                            .collect(Collectors.toList());

                    return BoardItemModelDto.builder()
                            .id(item.getId())
                            .code(item.getCode())
                            .state(state)
                            .itemName(item.getItemName())
                            .createDate(item.getCreateDate())
                            .updateDate(item.getUpdateDate())
                            .imagePath(item.getImagePath())
                            .itemImage(item.getItemImage())
                            .events(events)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private boolean stateExists(String code) {
        Optional<WorkflowState> workflowStateOptional = workflowStateRepository.findByCodeIgnoreCase(code);
        return workflowStateOptional.isPresent();
    }

    private boolean isEventNonDeletedInCalendar(String tenant, IBoardEvent boardEvent) {
        try {
            return Optional.ofNullable(
                            cmsCalendarEventService.eventByTenantAndCalendarAndCode(
                                    ContextRequestDto.builder().build(), tenant, boardEvent.getCalendar(), boardEvent.getEventCode()))
                    .filter(response -> {
                        if (!response.getStatusCode().is2xxSuccessful()) {
                            log.warn("Interview read failed: {}", boardEvent);
                            return false;
                        }
                        return true;
                    })
                    .map(ResponseEntity::getBody)
                    .isPresent();
        } catch (Exception e) {
            log.error("Remote feign call failed: ", e);
            return false;
        }
    }


    @Override
    public AccountModelDto getCandidateData(String code) {
        return jobApplicationRepository.findByCodeIgnoreCase(code)
                .map(jobOfferApplication -> AccountDto.builder()
                        .id(jobOfferApplication.getResume().getId())
                        .fullName(jobOfferApplication.getResume().getFullName())
                        .code(jobOfferApplication.getResume().getCode())
                        .email(jobOfferApplication.getResume().getEmail()).build())
                .orElseThrow(() -> new NotFoundException("Job application not found with code" + code));
    }

    @Override
    public JobOfferApplicationInterviewEventRequestDto getInterviewEvent(String tenant, String code, Long id) {
        // Use orElseThrow to simplify handling of Optional values and eliminate nested ifPresent() checks
        JobOfferApplicationEvent jobApplicationEvent = jobApplicationEventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Job application event not found with id" + id));

        JobOfferApplication jobApplication = jobApplicationRepository.findByCodeIgnoreCase(code)
                .orElseThrow(() -> new NotFoundException("Job application not found with code " + code));

        // Build DTO
        JobOfferApplicationInterviewEventRequestDto interviewEventRequestDto = JobOfferApplicationInterviewEventRequestDto.builder()
                .id(id)
                .tenant(tenant)
                .title(jobApplicationEvent.getTitle())
                .type(IEnumJobAppEventType.Types.INTERVIEW)
                .startDateTime(new Date())
                .endDateTime(new Date())
                .participants(jobApplicationEvent.getParticipants())
                .location(jobApplicationEvent.getLocation())
                .candidate(CandidateDto.builder()
                        .accountCode(resumeService.getResumeAccountCode(jobApplication.getResume().getCode()))
                        .id(jobApplication.getResume().getId())
                        .fullName(jobApplication.getResume().getFullName())
                        .code(jobApplication.getResume().getCode())
                        .email(jobApplication.getResume().getEmail())
                        .build())
                .comment(jobApplicationEvent.getComment())
                .build();

        Optional<Interview> optionalInterview = interviewEventRepository.findByJobApplicationEvent_Id(id);  // null if no interview is found

        // If there's an associated interview, set its details in the DTO
        optionalInterview.ifPresent(interview -> {
            interviewEventRequestDto.setQuizCode(interview.getQuizCode());
            interviewEventRequestDto.setSkills(interviewSkillsMapper.listEntityToDto(interview.getSkills()));
        });

        // Call the CMS service and update DTO if successful
        try {
            ResponseEntity<VCalendarEventDto> result = cmsCalendarEventService.eventByTenantAndCalendarAndCode(
                    ContextRequestDto.builder().build(),
                    interviewEventRequestDto.getTenant(),
                    interviewEventRequestDto.getType().name(),
                    jobApplicationEvent.getEventCode());

            if (!result.getStatusCode().is2xxSuccessful() && result.hasBody()) {
                log.warn("Interview creation failed");
            }

            VCalendarEventDto vCalendarEventDto = result.getBody();
            if (vCalendarEventDto != null) {
                interviewEventRequestDto.setStartDateTime(vCalendarEventDto.getStartDate());
                interviewEventRequestDto.setEndDateTime(vCalendarEventDto.getEndDate());
            } else {
                // Cleanup if no valid calendar event is returned
                jobApplicationEventRepository.deleteById(jobApplicationEvent.getId());
                optionalInterview.ifPresent(interview -> {
                    interviewEventRepository.deleteById(interview.getId());
                });
            }
        } catch (Exception e) {
            log.error("Remote feign call failed : ", e);
            // Optional: Uncomment the next line to throw an exception
            // throw new RemoteCallFailedException(e);
        }

        return interviewEventRequestDto;
    }


    @Override
    public JobOfferApplicationInterviewEventRequestDto addInterviewEvent(String tenant, String code, String eventType, JobOfferApplicationInterviewEventRequestDto event) {
        jobApplicationRepository.findByCodeIgnoreCase(code)
                .ifPresentOrElse(jobApp -> {
                            VCalendarEventDto vCalendarEventDto = VCalendarEventDto.builder()
                                    .name(event.getTitle())
                                    .title(event.getTitle())
                                    .tenant(tenant)
                                    .calendar(event.getType().name())
                                    .startDate(event.getStartDateTime())
                                    .endDate(event.getEndDateTime())
                                    .type(event.getType().name()).build();

                            try {
                                vCalendarEventDto = Optional.ofNullable(cmsCalendarEventService.saveEvent(ContextRequestDto.builder().build(),
                                                vCalendarEventDto))
                                        .filter(result -> result.getStatusCode().is2xxSuccessful() && result.hasBody())
                                        .map(result -> result.getBody())
                                        .orElse(vCalendarEventDto);
                            } catch (Exception e) {
                                log.error("Remote feign call failed : ", e);
                                //throw new RemoteCallFailedException(e);
                            }

                            List<InterviewSkills> interviewSkillsList = new ArrayList<>();

                            interviewSkillsList.addAll(Optional.ofNullable(jobApp.getJobOffer())
                                    .map(JobOffer::getDetails)
                                    .map(JobOfferDetails::getHardSkills)
                                    .orElse(Collections.emptyList()) // Avoid null checks
                                    .stream()
                                    .map(skill -> InterviewSkills.builder()
                                            .name(skill.getName())
                                            .type(IEnumInterviewSkillType.Types.JOB)
                                            .build())
                                    .collect(Collectors.toList()));

                            interviewSkillsList.addAll(Optional.ofNullable(jobApp.getJobOffer())
                                    .map(JobOffer::getDetails)
                                    .map(JobOfferDetails::getSoftSkills)
                                    .orElse(Collections.emptyList()) // Avoid null checks
                                    .stream()
                                    .map(skill -> InterviewSkills.builder()
                                            .name(skill.getName())
                                            .type(IEnumInterviewSkillType.Types.JOB)
                                            .build())
                                    .collect(Collectors.toList()));

                            interviewSkillsList.addAll(Optional.ofNullable(jobApp.getResume())
                                    .map(Resume::getDetails)
                                    .map(ResumeDetails::getSkills)
                                    .orElse(Collections.emptyList()) // Avoid null checks
                                    .stream()
                                    .map(skill -> InterviewSkills.builder()
                                            .name(skill.getName())
                                            .type(IEnumInterviewSkillType.Types.RESUME)
                                            .build())
                                    .collect(Collectors.toList()));

                            JobOfferApplicationEvent jobApplicationEvent = jobApplicationEventRepository.save(
                                    JobOfferApplicationEvent.builder()
                                            .eventCode(vCalendarEventDto.getCode())
                                            .calendar(vCalendarEventDto.getCalendar())
                                            .type(event.getType())
                                            .title(event.getTitle())
                                            .comment(event.getComment())
                                            .participants(event.getParticipants())
                                            .location(event.getLocation())
                                            .build()
                            );

                            //Setting event to job application :
                            List<JobOfferApplicationEvent> jobApplicationEventList = new ArrayList<>(jobApp.getJobApplicationEvents());
                            jobApplicationEventList.add(jobApplicationEvent);
                            jobApp.setJobApplicationEvents(jobApplicationEventList);

                            interviewEventRepository.save(
                                    Interview.builder()
                                            .jobApplicationEvent(jobApplicationEvent)
                                            .skills(interviewSkillsList)
                                            .quizCode(event.getQuizCode())
                                            .build()
                            );
                            jobApplicationRepository.save(jobApp);
                        },
                        () -> {
                            new JobOfferAppNotFoundException("with code : " + code);
                        });
        return event;
    }

    @Override
    public JobOfferApplicationInterviewEventRequestDto editInterviewEvent(String code, String eventType, JobOfferApplicationInterviewEventRequestDto event) {
        jobApplicationRepository.findByCodeIgnoreCase(code)
                .ifPresentOrElse(jobApp -> {
                            jobApplicationEventRepository.findById(event.getId())
                                    .ifPresent(jobOfferApplicationEvent -> {
                                        Optional<Interview> interviewOptional = interviewEventRepository.findByJobApplicationEvent_Id(event.getId());
                                        try {
                                            Optional.ofNullable(cmsCalendarEventService.eventByTenantAndCalendarAndCode(
                                                            ContextRequestDto.builder().build(),
                                                            jobApp.getTenant(),
                                                            jobOfferApplicationEvent.getCalendar(), jobOfferApplicationEvent.getEventCode()))
                                                    .filter(result -> result.getStatusCode().is2xxSuccessful() && result.hasBody())
                                                    .map(result -> result.getBody())
                                                    .ifPresentOrElse(vCalendarEventDto1 -> {
                                                                vCalendarEventDto1.setName(event.getTitle());
                                                                vCalendarEventDto1.setTitle(event.getTitle());
                                                                vCalendarEventDto1.setStartDate(event.getStartDateTime());
                                                                vCalendarEventDto1.setEndDate(event.getEndDateTime());
                                                                ResponseEntity<VCalendarEventDto> updateResult = cmsCalendarEventService.updateEvent(ContextRequestDto.builder().build(),
                                                                        vCalendarEventDto1.getId(),
                                                                        vCalendarEventDto1);
                                                                if (!updateResult.getStatusCode().is2xxSuccessful()) {
                                                                    log.warn("Interview update failed {}", event);
                                                                }
                                                            },
                                                            () -> {
                                                                jobApplicationEventRepository.deleteById(event.getId());
                                                                if (interviewOptional.isPresent()) {
                                                                    interviewEventRepository.deleteById(interviewOptional.get().getId());
                                                                }
                                                                throw new NotFoundException("Event not found with id : " + event.getId());
                                                            });
                                        } catch (Exception e) {
                                            log.error("Remote feign call failed : ", e);
                                            //throw new RemoteCallFailedException(e);
                                        }


                                        if (interviewOptional.isPresent()) {
                                            //TODO save dates into cms
                                            jobOfferApplicationEvent = jobApplicationEventRepository.save(
                                                    JobOfferApplicationEvent.builder()
                                                            .id(event.getId())
                                                            .eventCode(jobOfferApplicationEvent.getEventCode())
                                                            .calendar(jobOfferApplicationEvent.getCalendar())
                                                            .type(jobOfferApplicationEvent.getType())
                                                            .title(event.getTitle())
                                                            .comment(event.getComment())
                                                            .participants(event.getParticipants())
                                                            .location(event.getLocation())
                                                            .build()
                                            );

                                            interviewEventRepository.save(
                                                    Interview.builder()
                                                            .id(interviewOptional.get().getId())
                                                            .jobApplicationEvent(jobOfferApplicationEvent)
                                                            .quizCode(event.getQuizCode())
                                                            .skills(interviewSkillsMapper.listDtoToEntity(event.getSkills()))
                                                            .build()
                                            );
                                        }
                                    });
                        },
                        () -> {
                            throw new JobOfferAppNotFoundException("with code : " + code);
                        });
        return event;
    }


    @Override
    public List<String> getBoardWatchersByWorkflow(String workflowCode, String tenant) {
        return workflowBoardRepository.findByCodeIgnoreCase(tenant)
                .stream().filter(wfb -> wfb.getWorkflow().getCode().equals(workflowCode))
                .flatMap(workflowBoard -> workflowBoard.getWatchers().stream()).distinct()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<String> getItemTypes() {
        return null;
    }

    @Override
    public BpmEventResponseDto performEvent(
            BpmEventRequestDto bpmEventRequest) throws ClassNotFoundException, JsonProcessingException {
        log.info("Bpm event received: ", bpmEventRequest);

        //Check existence and get WorkflowBoard
        Optional<WorkflowBoard> workflowBoardOptional = workflowBoardRepository.findByCodeIgnoreCase(bpmEventRequest.getWbCode());
        if (!workflowBoardOptional.isPresent()) {
            throw new WorkflowBoardNotFoundException("with code " + bpmEventRequest.getWbCode());
        }
        WorkflowBoard workflowBoard = workflowBoardOptional.get();

        //Check existence and get workflow
        Workflow workflow = workflowBoard.getWorkflow();
        if (Objects.isNull(workflow)) {
            throw new WorkflowBoardWithNoWorkflowException("with code " + bpmEventRequest.getWbCode());
        }

        //Check existence and get workflowTransitions
        List<WorkflowTransition> workflowTransitions = workflow.getWorkflowTransitions();
        if (CollectionUtils.isEmpty(workflowTransitions)) {
            throw new WorkflowWithNoTransitionException("with code " + workflow.getCode());
        }

        //Check transition ability
        Optional<WorkflowTransition> workflowTransitionOptional = workflowTransitions.stream()
                .filter(wt -> (wt.getFromCode().equals(bpmEventRequest.getFromState()) && wt.getToCode().equals(bpmEventRequest.getToState())) ||
                        (wt.getFromCode().equals(bpmEventRequest.getToState()) && wt.getToCode().equals(bpmEventRequest.getFromState()) && wt.getBidirectional()))
                .findFirst();
        if (!workflowTransitionOptional.isPresent()) {
            //Transition not allowed return refuse response
            log.info("Transition from state {} to state {} is not allowed", bpmEventRequest.getFromState(), bpmEventRequest.getToState());
            return BpmEventResponseDto.builder()
                    .accepted(Boolean.FALSE)
                    .status(bpmEventRequest.getFromState())
                    .id(bpmEventRequest.getId())
                    .build();
        }

        WorkflowTransition workflowTransition = workflowTransitionOptional.get();
        //transition is allowed
        log.info("Transition from state {} to state {} is allowed", bpmEventRequest.getFromState(), bpmEventRequest.getToState());
        //Get Original Board item by Item type (class name)
        JpaPagingAndSortingTenantAndCodeAssignableRepository repository = (JpaPagingAndSortingTenantAndCodeAssignableRepository) genericRepository.getRepository(workflowBoard.getItem());
        Optional<IBoardItem> item = repository.findByCodeIgnoreCase(bpmEventRequest.getItem().getCode());
        if (!item.isPresent()) {
            throw new BoardItemNotFoundException(workflowBoard.getItem() + " with code " + bpmEventRequest.getItem().getCode());
        }

        //Update item state
        //TODO clear the code (remove double update )
        //bpmEventRequest.getItem().setState(bpmEventRequest.getToState());
        //IBoardItem tmpItem = item.get();
        item.get().setState(bpmEventRequest.getToState());
        IBoardItem boardItem = (IBoardItem) repository.save(item.get());

        //Check if should be notified
        if (workflowTransition.getNotify() != null && workflowTransition.getNotify()) {
            msgService.sendMessage(workflowBoard.getTenant(),
                    prepareNotifMessage(workflowBoard, workflowTransition, bpmEventRequest.getItem().getItemName()),
                    appProperties.isSendAsyncEmail());
        }

        //Return accept response
        return BpmEventResponseDto.builder()
                .accepted(Boolean.TRUE)
                .status((String) boardItem.getState())
                .id(boardItem.getId())
                .build();
    }

    private MailMessageDto prepareNotifMessage(WorkflowBoard workflowBoard, WorkflowTransition workflowTransition, String title) throws JsonProcessingException {
        //Send email/notification to the board watchers
        HashMap<String, String> variables = new HashMap<>();
        variables.put(MsgTemplateVariables.V_BOARD_NAME, workflowBoard.getName());
        variables.put(MsgTemplateVariables.V_ITEM_TITLE, title);
        variables.put(MsgTemplateVariables.V_FROM_STATE, workflowTransition.getFromCode());
        variables.put(MsgTemplateVariables.V_TO_STATE, workflowTransition.getToCode());

        return MailMessageDto.builder()
                .tenant(workflowBoard.getTenant())
                .subject(EmailSubjects.ITEM_STATUS_CHANGED_EMAIL_SUBJECT)
                .toAddr(String.join(",",
                        Stream.concat(workflowBoard.getWatchers().stream(),
                                workflowTransition.getWatchers().stream()).distinct().collect(Collectors.toUnmodifiableList())))
                .templateName(IEnumEmailTemplate.Types.WFB_UPDATED_TEMPLATE)
                .sent(true)
                .variables(MailMessageDto.getVariablesAsString(variables))
                .build();
    }

    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .tenant(TenantConstants.DEFAULT_TENANT_NAME)
                .entity(WorkflowBoard.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("WFB")
                .valueLength(6L)
                .codeValue(1L)
                .increment(1)
                .build();
    }
}
