

import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;

/**
 * Utility for client
 *
 */
public class ReferenceDataClientUtility {
	/**
	 * Get the responsebody content from Resource
	 * 
	 * @param response
	 * @return
	 */
	public static <E> Resources<E> getResponseBody(ResponseEntity<Resources<E>> response) {
		Resources<E> responseBody = null;
		if (Optional.ofNullable(response).isPresent()) {
			responseBody = response.getBody();
		}
		return responseBody;
	}
	
	public static <E> List<E> getResponseBodyList(ResponseEntity<List<E>> response) {
		List<E> responseBody = null;
		if (Optional.ofNullable(response).isPresent()) {
			responseBody = response.getBody();
		}
		return responseBody;
	}
	/**
	 * Get the responsebody content from Resource
	 * 
	 * @param response
	 * @return
	 */
	public static <E> Resources<E> getResponseBodyWithResources(ResponseEntity<Resources<E>> response) {
		Resources<E> responseBody = null;
		if (Optional.ofNullable(response).isPresent()) {
			responseBody = response.getBody();
		}
		return responseBody;
	}
}
