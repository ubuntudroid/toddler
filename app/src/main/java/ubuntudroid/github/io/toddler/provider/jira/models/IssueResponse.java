package ubuntudroid.github.io.toddler.provider.jira.models;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Don't convert this to kotlin as LoganSquare annotation processing doesn't work with it.
 *
 * Created by Sven Bendel.
 */
@JsonObject
public class IssueResponse {
    @JsonField
    public int total;

    @JsonField
    public int startAt;

    @JsonField
    public int maxResults;

    @JsonField
    public List<Issue> issues;

    @JsonObject
    public static class Issue {
        @JsonField
        public int id;

        @JsonField
        public String key;

        @JsonField
        public IssueFields fields;

        @JsonObject
        public static class IssueFields {
            @JsonField
            public String summary;
        }
    }
}
