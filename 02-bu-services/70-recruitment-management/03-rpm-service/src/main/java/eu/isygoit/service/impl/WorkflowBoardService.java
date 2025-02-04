package eu.isygoit.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.CodifiableService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.dto.common.*;
import eu.isygoit.dto.data.*;
import eu.isygoit.dto.extendable.AccountModelDto;
import eu.isygoit.enums.IEnumInterviewSkillType;
import eu.isygoit.enums.IEnumJobAppEventType;
import eu.isygoit.enums.IEnumMsgTemplateName;
import eu.isygoit.enums.IEnumPositionType;
import eu.isygoit.exception.*;
import eu.isygoit.mapper.InterviewSkillsMapper;
import eu.isygoit.model.*;
import eu.isygoit.model.extendable.NextCodeModel;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.cms.CmsCalendarEventService;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.*;
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
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = WorkflowBoardRepository.class)
public class WorkflowBoardService extends CodifiableService<Long, WorkflowBoard, WorkflowBoardRepository> implements IWorkflowBoardService {

    private final AppProperties appProperties;

    @Autowired
    private WorkflowBoardRepository workflowBoardRepository;
    @Autowired
    private WorkflowRepository workflowRepository;
    @Autowired
    private JobOfferApplicationEventRepository jobApplicationEventRepository;
    @Autowired
    private InterviewEventRepository interviewEventRepository;
    @Autowired
    private JobOfferApplicationRepository jobApplicationRepository;
    @Autowired
    private InterviewSkillsMapper interviewSkillsMapper;
    @Autowired
    private GenericRepository genericRepository;
    @Autowired
    private CmsCalendarEventService cmsCalendarEventService;
    @Autowired
    private IResumeService resumeService;

    @Autowired
    private IMsgService msgService;

    @Autowired
    private WorkflowStateRepository workflowStateRepository;

    public WorkflowBoardService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public WorkflowBoard beforeCreate(WorkflowBoard workflowBoard) {
        Optional<Workflow> optional = workflowRepository.findByCodeIgnoreCase(workflowBoard.getWorkflow().getCode());
        if (optional.isPresent()) {
            workflowBoard.setWorkflow(optional.get());
        }
        return super.beforeCreate(workflowBoard);
    }

    @Override
    public WorkflowBoard beforeUpdate(WorkflowBoard workflowBoard) {
        Optional<Workflow> optional = workflowRepository.findByCodeIgnoreCase(workflowBoard.getWorkflow().getCode());
        if (optional.isPresent()) {
            workflowBoard.setWorkflow(optional.get());
        }
        return super.beforeUpdate(workflowBoard);
    }

    @Override
    public List<WorkflowState> getStates(String wbCode) {
        Optional<WorkflowBoard> optional = this.workflowBoardRepository.findByCodeIgnoreCase(wbCode);
        if (optional.isPresent()) {
            WorkflowBoard workflowBoard = optional.get();
            return workflowBoard.getWorkflow().getWorkflowStates();
        }
        throw new WorkflowBoardNotFoundException("with code " + wbCode);
    }

    @Override
    public List<IBoardItem> getItems(String domain, String wbCode) throws ClassNotFoundException {
        Optional<WorkflowBoard> optional = this.workflowBoardRepository.findByCodeIgnoreCase(wbCode);
        if (optional.isPresent()) {
            WorkflowBoard workflowBoard = optional.get();
            if (workflowBoard.getWorkflow() == null) {
                throw new WorkflowNotParametrizeException("wb code " + wbCode);
            }

            if (CollectionUtils.isEmpty(workflowBoard.getWorkflow().getWorkflowStates())) {
                throw new WorkflowStatesNotParametrizeException("wf code " + workflowBoard.getWorkflow().getCode());
            }

            Optional<WorkflowState> workflowStateOptional = workflowBoard.getWorkflow().getWorkflowStates().stream()
                    .filter(workflowState -> workflowState.getPositionType().equals(IEnumPositionType.Types.INIT))
                    .findFirst();

            if (!workflowStateOptional.isPresent()) {
                workflowStateOptional = workflowBoard.getWorkflow().getWorkflowStates().stream().findFirst();
            }

            JpaPagingAndSortingSAASRepository repository = (JpaPagingAndSortingSAASRepository) genericRepository.getRepository(workflowBoard.getItem());
            Optional<WorkflowState> finalWorkflowStateOptional = workflowStateOptional;
            return (List<IBoardItem>) repository.findByDomainIgnoreCaseIn(Arrays.asList(domain)).stream().map(o -> BoardItemModelDto.builder()
                            .id(((IBoardItem) o).getId())
                            .code(((IBoardItem) o).getCode())
                            .state(((IBoardItem) o).getState() != null && stateExist((String) ((IBoardItem) o).getState()) ? (String) ((IBoardItem) o).getState() : finalWorkflowStateOptional.get().getCode())
                            .itemName(((IBoardItem) o).getItemName())
                            .createDate(((IBoardItem) o).getCreateDate())
                            .updateDate(((IBoardItem) o).getUpdateDate())
                            .imagePath(((IBoardItem) o).getImagePath())
                            .itemImage(((IBoardItem) o).getItemImage())
                            .events(((IBoardItem<?>) o).getEvents().stream()
                                    .map(e -> {
                                        if (isEventNonDeletedInCalendar(domain, e)) {
                                            return MiniBoardEventDto.builder()
                                                    .id(e.getId())
                                                    .title(e.getTitle())
                                                    .type(((Enum) e.getType()).name()).build();
                                        } else {
                                            return null;
                                        }
                                    })
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toUnmodifiableList())
                            )
                            .build())
                    .collect(Collectors.toUnmodifiableList());
        } else {
            throw new WorkflowBoardNotFoundException("with code " + wbCode);
        }
    }

    boolean stateExist(String code) {
        Optional<WorkflowState> workflowStateOptional = workflowStateRepository.findByCodeIgnoreCase(code);
        return workflowStateOptional.isPresent();
    }

    private boolean isEventNonDeletedInCalendar(String domain, IBoardEvent boardEvent) {
        try {
            ResponseEntity<VCalendarEventDto> result = cmsCalendarEventService.eventByDomainAndCalendarAndCode(RequestContextDto.builder().build(),
                    domain, boardEvent.getCalendar(), boardEvent.getEventCode());
            if (!result.getStatusCode().is2xxSuccessful()) {
                log.warn("Interview read failed {}", boardEvent);
            }
            VCalendarEventDto vCalendarEventDto = result.getBody();
            if (vCalendarEventDto == null) {
                //TODO delete element by type if doesn't exist
                return false;
            }
        } catch (Exception e) {
            log.error("Remote feign call failed : ", e);
            //throw new RemoteCallFailedException(e);
        }
        return true;
    }


    @Override
    public AccountModelDto getCandidateData(String code) {
        Optional<JobOfferApplication> jobApplicationOptional = jobApplicationRepository.findByCodeIgnoreCase(code);

        if (jobApplicationOptional.isPresent()) {
            Resume resume = jobApplicationOptional.get().getResume();
            return AccountDto.builder()
                    .id(resume.getId())
                    .fullName(resume.getFullName())
                    .code(resume.getCode())
                    .email(resume.getEmail()).build();
        }
        throw new NotFoundException("Job application not found with code" + code);
    }

    @Override
    public JobOfferApplicationInterviewEventRequestDto getInterviewEvent(String domain, String code, Long id) {
        Optional<JobOfferApplicationEvent> jobApplicationEventOptional = jobApplicationEventRepository.findById(id);
        Optional<JobOfferApplication> jobApplicationOptional = jobApplicationRepository.findByCodeIgnoreCase(code);

        if (jobApplicationEventOptional.isPresent() && jobApplicationOptional.isPresent()) {
            JobOfferApplicationEvent jobApplicationEvent = jobApplicationEventOptional.get();
            Resume resume = jobApplicationOptional.get().getResume();
            Optional<Interview> interviewOptional = interviewEventRepository.findByJobApplicationEvent_Id(id);

            JobOfferApplicationInterviewEventRequestDto interviewEventRequestDto = JobOfferApplicationInterviewEventRequestDto.builder()
                    //TODO change with JWT token domain
                    .id(id)
                    .domain(domain)
                    .title(jobApplicationEvent.getTitle())
                    .type(IEnumJobAppEventType.Types.INTERVIEW)
                    .startDateTime(new Date())
                    .endDateTime(new Date())
                    .participants(jobApplicationEvent.getParticipants())
                    .location(jobApplicationEvent.getLocation())
                    .candidate(CandidateDto.builder()
                            .accountCode(resumeService.getResumeAccountCode(resume.getCode()))
                            .id(resume.getId())
                            .fullName(resume.getFullName())
                            .code(resume.getCode())
                            .email(resume.getEmail()).build())
                    .comment(jobApplicationEvent.getComment()).build();
            if (interviewOptional.isPresent()) {
                Interview interview = interviewOptional.get();
                interviewEventRequestDto.setQuizCode(interview.getQuizCode());
                interviewEventRequestDto.setSkills(interviewSkillsMapper.listEntityToDto(interview.getSkills()));
            }
            try {
                ResponseEntity<VCalendarEventDto> result = cmsCalendarEventService.eventByDomainAndCalendarAndCode(RequestContextDto.builder().build(),
                        interviewEventRequestDto.getDomain(), interviewEventRequestDto.getType().name(), jobApplicationEvent.getEventCode());
                if (!result.getStatusCode().is2xxSuccessful() && result.hasBody()) {
                    log.warn("Interview creation failed");
                }
                VCalendarEventDto vCalendarEventDto = result.getBody();
                if (vCalendarEventDto != null) {
                    interviewEventRequestDto.setStartDateTime(vCalendarEventDto.getStartDate());
                    interviewEventRequestDto.setEndDateTime(vCalendarEventDto.getEndDate());
                } else {
                    jobApplicationEventRepository.deleteById(jobApplicationEvent.getId());
                    interviewEventRepository.deleteById(interviewOptional.get().getId());

                }
            } catch (Exception e) {
                log.error("Remote feign call failed : ", e);
                //throw new RemoteCallFailedException(e);
            }

            return interviewEventRequestDto;
        }
        throw new NotFoundException("Job application event not found with id" + id);
    }

    @Override
    public JobOfferApplicationInterviewEventRequestDto addInterviewEvent(String domain, String code, String eventType, JobOfferApplicationInterviewEventRequestDto event) {
        Optional<JobOfferApplication> jobApplication = jobApplicationRepository.findByCodeIgnoreCase(code);
        VCalendarEventDto vCalendarEventDto = VCalendarEventDto.builder()
                .name(event.getTitle())
                .title(event.getTitle())
                .domain(domain)
                .calendar(event.getType().name())
                .startDate(event.getStartDateTime())
                .endDate(event.getEndDateTime())
                .type(event.getType().name()).build();
        try {
            ResponseEntity<VCalendarEventDto> result = cmsCalendarEventService.saveEvent(//RequestContextDto.builder().build(),
                    vCalendarEventDto);
            if (!result.getStatusCode().is2xxSuccessful()) {
                log.warn("Interview creation failed {}", event);
            }
            vCalendarEventDto = result.getBody();
        } catch (Exception e) {
            log.error("Remote feign call failed : ", e);
            //throw new RemoteCallFailedException(e);
        }


        if (jobApplication.isPresent()) {
            JobOfferApplication jobApp = jobApplication.get();
            List<InterviewSkills> interviewSkillsList = new ArrayList<>();
            if (jobApp.getJobOffer() != null && jobApp.getJobOffer().getDetails() != null && !CollectionUtils.isEmpty(jobApp.getJobOffer().getDetails().getHardSkills())) {
                jobApp.getJobOffer().getDetails().getHardSkills().forEach(skill -> {
                    interviewSkillsList.add(InterviewSkills.builder()
                            .name(skill.getName())
                            .type(IEnumInterviewSkillType.Types.JOB)
                            .build());
                });
            }

            if (jobApp.getJobOffer() != null && jobApp.getJobOffer().getDetails() != null && !CollectionUtils.isEmpty(jobApp.getJobOffer().getDetails().getSoftSkills())) {
                jobApp.getJobOffer().getDetails().getSoftSkills().forEach(skill -> {
                    interviewSkillsList.add(InterviewSkills.builder()
                            .name(skill.getName())
                            .type(IEnumInterviewSkillType.Types.JOB)
                            .build());
                });
            }

            if (jobApp.getResume() != null && jobApp.getResume().getDetails() != null && !CollectionUtils.isEmpty(jobApp.getResume().getDetails().getSkills())) {
                jobApp.getResume().getDetails().getSkills().forEach(skill -> {
                    interviewSkillsList.add(InterviewSkills.builder()
                            .name(skill.getName())
                            .type(IEnumInterviewSkillType.Types.RESUME)
                            .build());
                });
            }

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
            List<JobOfferApplicationEvent> jobApplicationEventList = new ArrayList<>(jobApplication.get().getJobApplicationEvents());
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
            return event;
        }
        throw new NotFoundException("Job application not found with code : " + code);
    }

    @Override
    public JobOfferApplicationInterviewEventRequestDto editInterviewEvent(String code, String eventType, JobOfferApplicationInterviewEventRequestDto event) {
        Optional<JobOfferApplication> jobApplication = jobApplicationRepository.findByCodeIgnoreCase(code);
        if (jobApplication.isPresent()) {
            Optional<JobOfferApplicationEvent> jobApplicationEventOptional = jobApplicationEventRepository.findById(event.getId());
            if (jobApplicationEventOptional.isPresent()) {
                JobOfferApplicationEvent jobApplicationEvent = jobApplicationEventOptional.get();
                Optional<Interview> interviewOptional = interviewEventRepository.findByJobApplicationEvent_Id(event.getId());
                try {
                    ResponseEntity<VCalendarEventDto> result = cmsCalendarEventService.eventByDomainAndCalendarAndCode(
                            RequestContextDto.builder().build(),
                            jobApplication.get().getDomain(),
                            jobApplicationEvent.getCalendar(), jobApplicationEvent.getEventCode());
                    if (!result.getStatusCode().is2xxSuccessful()) {
                        log.warn("Interview read failed {}", event);
                    }

                    VCalendarEventDto vCalendarEventDto = result.getBody();
                    if (vCalendarEventDto != null) {
                        vCalendarEventDto.setName(event.getTitle());
                        vCalendarEventDto.setTitle(event.getTitle());
                        vCalendarEventDto.setStartDate(event.getStartDateTime());
                        vCalendarEventDto.setEndDate(event.getEndDateTime());
                        ResponseEntity<VCalendarEventDto> updateResult = cmsCalendarEventService.updateEvent(//RequestContextDto.builder().build(),
                                vCalendarEventDto.getId(),
                                vCalendarEventDto);
                        if (!updateResult.getStatusCode().is2xxSuccessful()) {
                            log.warn("Interview update failed {}", event);
                        }
                    } else {
                        jobApplicationEventRepository.deleteById(event.getId());
                        interviewEventRepository.deleteById(interviewOptional.get().getId());
                        throw new NotFoundException("Event not found with id : " + event.getId());
                    }
                } catch (Exception e) {
                    log.error("Remote feign call failed : ", e);
                    //throw new RemoteCallFailedException(e);
                }


                if (interviewOptional.isPresent()) {
                    //TODO save dates into cms
                    jobApplicationEvent = jobApplicationEventRepository.save(
                            JobOfferApplicationEvent.builder()
                                    .id(event.getId())
                                    .eventCode(jobApplicationEvent.getEventCode())
                                    .calendar(jobApplicationEvent.getCalendar())
                                    .type(jobApplicationEvent.getType())
                                    .title(event.getTitle())
                                    .comment(event.getComment())
                                    .participants(event.getParticipants())
                                    .location(event.getLocation())
                                    .build()
                    );

                    interviewEventRepository.save(
                            Interview.builder()
                                    .id(interviewOptional.get().getId())
                                    .jobApplicationEvent(jobApplicationEvent)
                                    .quizCode(event.getQuizCode())
                                    .skills(interviewSkillsMapper.listDtoToEntity(event.getSkills()))
                                    .build()
                    );
                    return event;
                }
            }
        }
        throw new NotFoundException("Job application not found with code : " + code);
    }


    @Override
    public List<String> getBoardWatchersByWorkflow(String workflowCode, String domain) {
        return workflowBoardRepository.findByCodeIgnoreCase(domain)
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
        if (workflow == null) {
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
        JpaPagingAndSortingSAASCodifiableRepository repository = (JpaPagingAndSortingSAASCodifiableRepository) genericRepository.getRepository(workflowBoard.getItem());
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
            msgService.sendMessage(workflowBoard.getDomain(),
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
                .domain(workflowBoard.getDomain())
                .subject(EmailSubjects.ITEM_STATUS_CHANGED_EMAIL_SUBJECT)
                .toAddr(String.join(",",
                        Stream.concat(workflowBoard.getWatchers().stream(),
                                workflowTransition.getWatchers().stream()).distinct().collect(Collectors.toUnmodifiableList())))
                .templateName(IEnumMsgTemplateName.Types.WFB_UPDATED_TEMPLATE)
                .sent(true)
                .variables(MailMessageDto.getVariablesAsString(variables))
                .build();
    }

    @Override
    public Optional<NextCodeModel> initCodeGenerator() {
        return Optional.ofNullable(AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(WorkflowBoard.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("WFB")
                .valueLength(6L)
                .value(1L)
                .increment(1)
                .build());
    }
}
