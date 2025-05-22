package eu.isygoit.dto.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private long totalTemplates;
    private long totalCategories;
    private long activeAuthors;
//    private long pendingTemplates;
    long pinnedTemplates;
    private Map<String, Long> documentFormats;
    private Map<String, Long> languageStats;
    private List<RecentTemplateDTO> recentTemplates;
    private List<CategoryStatsDTO> topCategories;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentTemplateDTO {
        private Long id;
        private String name;
        private String category;
        private String author;
        private String status;
        private String date;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryStatsDTO {
        private String name;
        private long count;
        private String color;
    }
}