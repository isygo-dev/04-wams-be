package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.CrudService;
import eu.isygoit.dto.data.QuizDto;
import eu.isygoit.dto.data.QuizQuestionDto;
import eu.isygoit.dto.data.QuizReportDto;
import eu.isygoit.dto.data.QuizSectionReportDto;
import eu.isygoit.enums.IEnumQuestionType;
import eu.isygoit.exception.*;
import eu.isygoit.helper.DateHelper;
import eu.isygoit.mapper.QuizMapper;
import eu.isygoit.model.CandidateQuiz;
import eu.isygoit.model.CandidateQuizAnswer;
import eu.isygoit.model.Quiz;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.CandidateQuizAnswerRepository;
import eu.isygoit.repository.CandidateQuizRepository;
import eu.isygoit.repository.QuizRepository;
import eu.isygoit.service.ICandidateQuizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Candidate quiz service.
 */
@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = CandidateQuizRepository.class)
public class CandidateQuizService extends CrudService<Long, CandidateQuiz, CandidateQuizRepository> implements ICandidateQuizService {

    @Autowired
    private CandidateQuizAnswerRepository candidateQuizAnswerRepository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuizMapper quizMapper;

    @Override
    public CandidateQuiz beforeCreate(CandidateQuiz candidateQuiz) {
        candidateQuiz.setStartDate(new Date());
        return super.beforeCreate(candidateQuiz);
    }

    @Override
    public CandidateQuiz findByQuizCodeAndAccountCode(String quizCode, String accountCode) {
        Optional<CandidateQuiz> optional = repository().findByQuizCodeIgnoreCaseAndAccountCodeIgnoreCase(quizCode, accountCode);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public boolean startCandidateAnswer(String quizCode, String accountCode, CandidateQuizAnswer answer) {
        //Check if Quiz is already started for candidate
        CandidateQuiz candidateQuiz = this.findByQuizCodeAndAccountCode(quizCode, accountCode);
        if (candidateQuiz == null) {
            throw new CandidateQuizNotYetStartedException("with code: " + accountCode + "/" + quizCode);
        }

        //Create empty answer list
        if (CollectionUtils.isEmpty(candidateQuiz.getAnswers())) {
            candidateQuiz.setAnswers(new ArrayList<>());
        }

        //Check if candidate answer is already started => Exception
        Optional<CandidateQuizAnswer> optionalCandidateQuizAnswer = candidateQuiz.getAnswers().stream().parallel()
                .filter(candidateQuizAnswer ->
                        candidateQuizAnswer.getQuestion() != null && candidateQuizAnswer.getQuestion().equals(answer.getQuestion())).findFirst();
        if (optionalCandidateQuizAnswer.isPresent()) {
            throw new CandidateAnswerExistsException("with code: " + accountCode + "/" + quizCode + "/" + answer.getQuestion());
        }

        //Set start date for the answer and save
        answer.setStartDate(new Date());
        candidateQuiz.getAnswers().add(answer);
        this.saveOrUpdate(candidateQuiz);

        return true;
    }

    @Override
    public boolean submitCandidateAnswer(String quizCode, String accountCode, CandidateQuizAnswer answer) {
        //Check if Quiz is already started for candidate
        CandidateQuiz candidateQuiz = this.findByQuizCodeAndAccountCode(quizCode, accountCode);
        if (candidateQuiz == null) {
            throw new CandidateQuizNotYetStartedException("with code: " + accountCode + "/" + quizCode);
        }

        //Create empty answer list
        if (CollectionUtils.isEmpty(candidateQuiz.getAnswers())) {
            candidateQuiz.setAnswers(new ArrayList<>());
        }

        //Check if candidate answer is already started : If not => Exception
        Optional<CandidateQuizAnswer> optionalCandidateQuizAnswer = candidateQuiz.getAnswers().stream().parallel()
                .filter(candidateQuizAnswer ->
                        candidateQuizAnswer.getQuestion() != null && candidateQuizAnswer.getQuestion().equals(answer.getQuestion())).findFirst();
        if (!optionalCandidateQuizAnswer.isPresent()) {
            throw new CandidateAnswerNotYetStartedException("with code: " + accountCode + "/" + quizCode + "/" + answer.getQuestion());
        }

        //Attach to the existing answer and save
        answer.setId(optionalCandidateQuizAnswer.get().getId());
        answer.setStartDate(optionalCandidateQuizAnswer.get().getStartDate());
        answer.setSubmitDate(new Date());
        candidateQuizAnswerRepository.save(answer);
        return true;
    }

    @Override
    public boolean submitCandidateAnswers(String quizCode, String accountCode, List<CandidateQuizAnswer> answers) {
        if (CollectionUtils.isEmpty(answers)) {
            log.warn("Ansewer list is empty! quiz {}/account {}", quizCode, accountCode);
            return false;
        }
        answers.stream().forEach(candidateQuizAnswer -> {
            //Check if Quiz is already started for candidate otherwise start it (!! INTERVIEW)
            CandidateQuiz candidateQuiz = this.findByQuizCodeAndAccountCode(quizCode, accountCode);
            if (candidateQuiz == null) {
                this.startCandidateQuiz(quizCode, accountCode);
                candidateQuiz = this.findByQuizCodeAndAccountCode(quizCode, accountCode);
            }

            //Create empty answer list
            if (CollectionUtils.isEmpty(candidateQuiz.getAnswers())) {
                candidateQuiz.setAnswers(new ArrayList<>());
            }

            //Check if candidate answer is already started : If not => Exception
            Optional<CandidateQuizAnswer> optionalCandidateQuizAnswer = candidateQuiz.getAnswers().stream().parallel()
                    .filter(candQuizAnswer ->
                            candQuizAnswer.getQuestion() != null && candQuizAnswer.getQuestion().equals(candidateQuizAnswer.getQuestion())).findFirst();
            if (!optionalCandidateQuizAnswer.isPresent()) {
                this.startCandidateAnswer(quizCode, accountCode, candidateQuizAnswer);
            } else {
                //Attach to the existing answer and save
                candidateQuizAnswer.setId(optionalCandidateQuizAnswer.get().getId());
                candidateQuizAnswer.setStartDate(optionalCandidateQuizAnswer.get().getStartDate());
                //Never submit answer for Quiz interview type
                candidateQuizAnswerRepository.save(candidateQuizAnswer);
            }
        });
        return true;
    }

    @Override
    public Long startCandidateQuiz(String quizCode, String accountCode) {
        //Check if Quiz is already started for candidate => Exception
        CandidateQuiz candidateQuiz = this.findByQuizCodeAndAccountCode(quizCode, accountCode);
        if (candidateQuiz != null) {
            throw new CandidateQuizAlreadyStartedException("with code: " + accountCode + "/" + quizCode);
        }
        candidateQuiz = this.create(CandidateQuiz.builder()
                .accountCode(accountCode)
                .quizCode(quizCode)
                .startDate(new Date())
                .build());

        return candidateQuiz.getId();
    }

    @Override
    public boolean submitCandidateQuiz(String quizCode, String accountCode) {
        //Check if Quiz is not yet started for candidate => Exception
        CandidateQuiz candidateQuiz = this.findByQuizCodeAndAccountCode(quizCode, accountCode);
        if (candidateQuiz == null) {
            throw new CandidateQuizNotYetStartedException("with code: " + accountCode + "/" + quizCode);
        }

        candidateQuiz.setSubmitDate(new Date());
        this.update(candidateQuiz);
        return true;
    }

    @Override
    public QuizDto getCompleteAnswer(String quizCode, String accountCode) {
        Optional<Quiz> optionalQuiz = quizRepository.findByCodeIgnoreCase(quizCode);
        if (optionalQuiz.isPresent()) {
            Optional<CandidateQuiz> optionalCandidateQuiz = repository().findByQuizCodeIgnoreCaseAndAccountCodeIgnoreCase(quizCode, accountCode);
            if (optionalCandidateQuiz.isPresent()) {
                QuizDto completeAnswer = quizMapper.entityToDto(optionalQuiz.get());
                CandidateQuiz candidateQuiz = optionalCandidateQuiz.get();
                completeAnswer.setStartDate(candidateQuiz.getStartDate());
                completeAnswer.setSubmitDate(candidateQuiz.getSubmitDate());
                completeAnswer.getSections().stream().forEach(quizSection -> {
                    quizSection.getQuestions().stream().forEach(quizQuestion -> {
                        List<CandidateQuizAnswer> answers = candidateQuiz.getAnswers().stream().filter(candidateQuizAnswer ->
                                candidateQuizAnswer.getQuestion() != null && candidateQuizAnswer.getQuestion().equals(quizQuestion.getId())).collect(Collectors.toList());
                        if (quizQuestion.getType() == IEnumQuestionType.Types.TAQ || quizQuestion.getType() == IEnumQuestionType.Types.CODE) {
                            if (!CollectionUtils.isEmpty(answers) && answers.size() == 1 && answers.get(0).getOption() == 0) {
                                quizQuestion.setTextAnswer(answers.get(0).getAnswerText());
                            } else {
                                quizQuestion.setTextAnswer(null);
                            }
                        } else {
                            quizQuestion.getOptions().stream().forEach(quizOption -> {
                                Optional<CandidateQuizAnswer> optional = answers.stream().filter(candidateQuizAnswer ->
                                        candidateQuizAnswer.getOption() != null && candidateQuizAnswer.getOption().equals(quizOption.getId())).findFirst();
                                if (optional.isPresent()) {
                                    if (quizOption.getChecked()) {
                                        quizQuestion.setScore(quizQuestion.getScore() + 1);
                                    }
                                    quizOption.setChecked(true);
                                    quizOption.setTextAnswer(optional.get().getAnswerText());
                                } else {
                                    quizOption.setChecked(false);
                                    quizOption.setTextAnswer(null);
                                }
                            });
                        }
                    });
                });
                return completeAnswer;
            }
        }
        return null;
    }

    @Override
    public QuizDto getCompleteAnswerClean(String quizCode, String accountCode) {
        Optional<Quiz> optionalQuiz = quizRepository.findByCodeIgnoreCase(quizCode);
        if (optionalQuiz.isPresent()) {
            Optional<CandidateQuiz> optionalCandidateQuiz = repository().findByQuizCodeIgnoreCaseAndAccountCodeIgnoreCase(quizCode, accountCode);
            if (optionalCandidateQuiz.isPresent()) {
                QuizDto completeAnswer = quizMapper.entityToDto(optionalQuiz.get());
                CandidateQuiz candidateQuiz = optionalCandidateQuiz.get();
                completeAnswer.setStartDate(candidateQuiz.getStartDate());
                completeAnswer.setSubmitDate(candidateQuiz.getSubmitDate());
                completeAnswer.getSections().stream().forEach(quizSection -> {
                    quizSection.getQuestions().stream().forEach(quizQuestion -> {
                        List<CandidateQuizAnswer> answers = candidateQuiz.getAnswers().stream().filter(candidateQuizAnswer ->
                                candidateQuizAnswer.getQuestion() != null && candidateQuizAnswer.getQuestion().equals(quizQuestion.getId())).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(answers) && answers.size() == 1 && answers.get(0).getOption() == 0) {
                            quizQuestion.setTextAnswer(answers.get(0).getAnswerText());
                            quizQuestion.setScore(answers.get(0).getScore());
                        } else {
                            quizQuestion.setTextAnswer(null);
                            quizQuestion.setScore(0D);
                        }
                    });
                });
                return completeAnswer;
            }
        }
        return null;
    }

    @Override
    public QuizReportDto getReport(String quizCode, String accountCode) {
        QuizDto completeAnswer = getCompleteAnswer(quizCode, accountCode);
        if (completeAnswer != null) {
            QuizReportDto quizReport = QuizReportDto.builder()
                    .quizCode(quizCode)
                    .accountCode(accountCode)
                    .name(completeAnswer.getName())
                    .startDate(completeAnswer.getStartDate())
                    .submitDate(completeAnswer.getSubmitDate())
                    .description(completeAnswer.getDescription())
                    .tags(completeAnswer.getTags())
                    .score(completeAnswer.getScore())
                    .build();
            completeAnswer.getSections().forEach(quizSection -> {
                quizReport.getSectionReports().add(QuizSectionReportDto.builder()
                        .name(quizSection.getName())
                        .description(quizSection.getDescription())
                        .score(quizSection.getScore())
                        .scale((double) quizSection.getQuestions().size())
                        .build());
            });
            return quizReport;
        }
        return null;
    }

    @Override
    public QuizDto getCandidateQuiz(String quizCode, String accountCode) {
        Optional<Quiz> optionalQuiz = quizRepository.findByCodeIgnoreCase(quizCode);
        if (optionalQuiz.isPresent()) {
            QuizDto quiz = quizMapper.entityToDto(optionalQuiz.get());
            CandidateQuiz candidateQuiz = findByQuizCodeAndAccountCode(quizCode, accountCode);
            if (candidateQuiz != null) {
                quiz.getSections().forEach(quizSection -> {
                    List<QuizQuestionDto> questionsToComplete = new ArrayList<>();
                    quizSection.getQuestions().stream().forEach(quizQuestion -> {
                        Optional<CandidateQuizAnswer> optionalCandidateQuizAnswer = candidateQuiz.getAnswers().stream().parallel()
                                .filter(candidateQuizAnswer ->
                                        candidateQuizAnswer.getQuestion() != null && candidateQuizAnswer.getQuestion().equals(quizQuestion.getId())).findFirst();
                        if (!optionalCandidateQuizAnswer.isPresent() || "interview".equals(quiz.getCategory())) {
                            questionsToComplete.add(quizQuestion);
                        } else {
                            if (optionalCandidateQuizAnswer.get().getSubmitDate() == null) {
                                Long passed = DateHelper.between(optionalCandidateQuizAnswer.get().getStartDate(), new Date());
                                quizQuestion.setRemainInSec(Math.max(0, quizQuestion.getDurationInSec() - passed));
                                if (passed < quizQuestion.getDurationInSec() - 10) {
                                    questionsToComplete.add(quizQuestion);
                                }
                            }

                        }
                    });
                    quizSection.setQuestions(questionsToComplete);
                });
            }
            return quiz;
        }
        throw new QuizNotFoundException("with code: " + quizCode);
    }

    @Override
    public List<QuizReportDto> getByCandidateAndTags(String accountCode, List<String> tags) {
        List<Quiz> elligibleQuizes = quizRepository.findByTagsIn(tags);
        if (CollectionUtils.isEmpty(elligibleQuizes)) {
            return Collections.EMPTY_LIST;
        }

        List<QuizReportDto> candidateQuizs = new ArrayList<>();
        elligibleQuizes.forEach(quiz -> {
            QuizReportDto quizReportDto = getReport(quiz.getCode(), accountCode);
            if (quizReportDto != null) {
                candidateQuizs.add(quizReportDto);
            } else {
                candidateQuizs.add(QuizReportDto.builder()
                        .quizCode(quiz.getCode())
                        .accountCode(accountCode)
                        .name(quiz.getName())
                        .description(quiz.getDescription())
                        .tags(quiz.getTags())
                        .build());
            }
        });

        return candidateQuizs;
    }

    @Override
    public Long getCountRealizedTestByAccount(String accountCode) {
        return repository().countByAccountCode(accountCode);
    }
}