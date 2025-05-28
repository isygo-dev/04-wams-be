package eu.isygoit.service.impl;

import eu.isygoit.dto.data.DashboardStatsDTO;
import eu.isygoit.enums.IEnumTemplateStatus;
import eu.isygoit.model.Author;
import eu.isygoit.model.Category;
import eu.isygoit.model.Template;
import eu.isygoit.repository.AuthorRepository;
import eu.isygoit.repository.CategoryRepository;
import eu.isygoit.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TemplateRepository templateRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;

    public DashboardStatsDTO getDashboardStatistics(String userName) {
        List<Template> templates = templateRepository.findAll();
        List<Category> categories = categoryRepository.findAll();
        List<Author> authors = authorRepository.findAll();

        long totalTemplates = templates.size();
        long totalCategories = categories.size();
        long activeAuthors = authors.size();
        long pinnedTemplates = 0;
        if (userName != null && !userName.isEmpty()) {
            pinnedTemplates = templateRepository.countByFavoritesContaining(userName);
        }

        Map<String, Long> documentFormats = templates.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getExtension() != null ? t.getExtension().toLowerCase() : "unknown",
                        Collectors.counting()
                ));

        Map<String, Long> languageStats = templates.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getLanguage() != null ? t.getLanguage().name() : "unknown",
                        Collectors.counting()
                ));

        List<DashboardStatsDTO.RecentTemplateDTO> recentTemplates = templates.stream()
                .sorted(Comparator.comparing(Template::getCreateDate).reversed())
                .limit(5)
                .map(t -> {
                    String formattedDate = "N/A";
                    if (t.getCreateDate() != null) {
                        formattedDate = new SimpleDateFormat("dd MMM yyyy").format(t.getCreateDate());
                    }
                    return DashboardStatsDTO.RecentTemplateDTO.builder()
                            .id(t.getId())
                            .name(t.getName())
                            .category(t.getCategory() != null ? t.getCategory().getName() : "Non catégorisé")
                            .author(t.getAuthor() != null ?
                                    t.getAuthor().getFirstName() + " " + t.getAuthor().getLastName() : "Anonyme")
                            .status(t.getState() == IEnumTemplateStatus.Types.VALIDATING ? "active" : "pending")
                            .date(formattedDate)
                            .build();
                })
                .collect(Collectors.toList());

        List<DashboardStatsDTO.CategoryStatsDTO> topCategories = categories.stream()
                .map(c -> new DashboardStatsDTO.CategoryStatsDTO(
                        c.getName(),
                        templates.stream().filter(t ->
                                t.getCategory() != null && t.getCategory().getId().equals(c.getId())
                        ).count(),
                        "primary"
                ))
                .sorted((a, b) -> Long.compare(b.getCount(), a.getCount()))
                .limit(4)
                .collect(Collectors.toList());

        return new DashboardStatsDTO(
                totalTemplates,
                totalCategories,
                activeAuthors,
//                pendingTemplates,
                pinnedTemplates,
                documentFormats,
                languageStats,
                recentTemplates,
                topCategories
        );
    }
}