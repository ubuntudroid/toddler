package ubuntudroid.github.io.toddler.provider.jira;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;
import rx.Observable;
import ubuntudroid.github.io.toddler.provider.jira.models.IssueResponse;

/**
 * Created by Sven Bendel.
 */
public interface JiraService {
    @Headers("Content-Type: application/json")
    @GET("/rest/api/2/search")
    Observable<IssueResponse> performJqlQuery ( @Query("jql") String query, @Query("startAt") int startAt, @Query("maxResults") int maxResults );
}
