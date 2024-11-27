package eu.isygoit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.isygoit.com.rest.service.ICrudServiceMethod;
import eu.isygoit.dto.common.BpmEventRequestDto;
import eu.isygoit.dto.common.BpmEventResponseDto;
import eu.isygoit.dto.data.JobOfferApplicationInterviewEventRequestDto;
import eu.isygoit.dto.extendable.AccountModelDto;
import eu.isygoit.model.IBoardItem;
import eu.isygoit.model.WorkflowBoard;
import eu.isygoit.model.WorkflowState;

import java.util.List;

/**
 * The interface Workflow board service.
 */
public interface IWorkflowBoardService extends ICrudServiceMethod<Long, WorkflowBoard> {

    /**
     * Gets states.
     *
     * @param wbCode the wb code
     * @return the states
     */
    List<WorkflowState> getStates(String wbCode);

    /**
     * Gets items.
     *
     * @param domain the domain
     * @param wbCode the wb code
     * @return the items
     * @throws ClassNotFoundException the class not found exception
     */
    List<IBoardItem> getItems(String domain, String wbCode) throws ClassNotFoundException;

    /**
     * Gets interview event.
     *
     * @param domain the domain
     * @param code   the code
     * @param id     the id
     * @return the interview event
     */
    JobOfferApplicationInterviewEventRequestDto getInterviewEvent(String domain, String code, Long id);

    /**
     * Add interview event job offer application interview event request dto.
     *
     * @param domain    the domain
     * @param code      the code
     * @param eventType the event type
     * @param event     the event
     * @return the job offer application interview event request dto
     */
    JobOfferApplicationInterviewEventRequestDto addInterviewEvent(String domain, String code, String eventType, JobOfferApplicationInterviewEventRequestDto event);

    /**
     * Edit interview event job offer application interview event request dto.
     *
     * @param code      the code
     * @param eventType the event type
     * @param event     the event
     * @return the job offer application interview event request dto
     */
    JobOfferApplicationInterviewEventRequestDto editInterviewEvent(String code, String eventType, JobOfferApplicationInterviewEventRequestDto event);

    /**
     * Gets board watchers by workflow.
     *
     * @param workflowCode the workflow code
     * @param domain       the domain
     * @return the board watchers by workflow
     */
    List<String> getBoardWatchersByWorkflow(String workflowCode, String domain);

    /**
     * Gets item types.
     *
     * @return the item types
     */
    List<String> getItemTypes();

    /**
     * Perform event bpm event response dto.
     *
     * @param bpmEventRequest the bpm event request
     * @return the bpm event response dto
     * @throws ClassNotFoundException  the class not found exception
     * @throws JsonProcessingException the json processing exception
     */
    BpmEventResponseDto performEvent(BpmEventRequestDto bpmEventRequest) throws ClassNotFoundException, JsonProcessingException;

    /**
     * Gets candidate data.
     *
     * @param code the code
     * @return the candidate data
     */
    AccountModelDto getCandidateData(String code);
}
