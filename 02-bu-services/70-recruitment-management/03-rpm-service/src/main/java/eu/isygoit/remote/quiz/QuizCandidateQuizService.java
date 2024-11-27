package eu.isygoit.remote.quiz;

import eu.isygoit.api.CandidateQuizControllerApi;
import eu.isygoit.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * The interface Quiz candidate quiz service.
 */
@FeignClient(configuration = FeignConfig.class, name = "quiz-service", contextId = "candidate-quiz", path = "/api/v1/private/candidate/quiz")
public interface QuizCandidateQuizService extends CandidateQuizControllerApi {

}
