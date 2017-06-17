
import static com.test.boot.refdata.utility.ClientUtility.getResponseBody;
import static com.test.boot.refdata.utility.ClientUtility.getResponseBodyList;
import static com.test.boot.refdata.utility.ClientUtility.getResponseBodyWithResources;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;;

/**
 * Client for  data.
 *
 */
@Slf4j
@Component
public class OrderReferenceDataClient {

	@Autowired
	private OrderReferenceDataProperties referenceDataProperties;

	@Autowired
	@Qualifier("referenceDataRestTemplate")
	private RestTemplate referenceDataRestTemplate;

	/**
	 * Find all the comment types
	 * 
	 * @return List of CommentType
	 */
	@HystrixCommand
	@Cacheable(value="commenttypeslowfrequency", cacheManager="orderManagementReferenceDataCacheManager")
	public Resources<CommentType> findAllCommentTypes() {
		log.debug("Reference data client method to find all comment types");
		ResponseEntity<Resources<CommentType>> response = referenceDataRestTemplate.exchange(
				referenceDataProperties.getBaseURL() + "/commentTypes/search/findByCommentTypeCodeNotIn",
				HttpMethod.GET, null, new ParameterizedTypeReference<Resources<CommentType>>() {
				}, Collections.emptyMap());

		return getResponseBody(response);
	}

	/**
	 * Find all the comment templates
	 * 
	 * @return List of comment templates
	 */
	@HystrixCommand
	public Resources<CommentTemplate> findAllCommentTemplates(String commentTypeCode) {
		log.debug("Reference data client method to find all comment templates by comment type code");
		Map<String, String> parameter = new HashMap<>();
		parameter.put("commentTypeCode", commentTypeCode);
		ResponseEntity<Resources<CommentTemplate>> response = referenceDataRestTemplate.exchange(
				referenceDataProperties.getBaseURL() + "/commentTemplates?commentTypeCode={commentTypeCode}",
				HttpMethod.GET, null, new ParameterizedTypeReference<Resources<CommentTemplate>>() {
				}, parameter);
		return getResponseBody(response);
	}

	/**
	 * Finds All the Service Type based on the Service Category Code
	 * 
	 * @param serviceCategoryCode
	 * @return
	 */
	@HystrixCommand
	public Resources<ServiceType> findAllServiceTypes(String serviceCategoryCode) {
		log.debug("Reference data client method to find the Service Types by Service Category");

		Map<String, Object> parameter = new HashMap<>();
		parameter.put("serviceCategoryCode", serviceCategoryCode);
		ResponseEntity<Resources<ServiceType>> response = referenceDataRestTemplate.exchange(
				referenceDataProperties.getBaseURL()
						+ "/serviceTypes/search/findByServiceCategoryServiceCategoryCode?serviceCategoryCode={serviceCategoryCode}",
				HttpMethod.GET, null, new ParameterizedTypeReference<Resources<ServiceType>>() {
				}, parameter);
		return getResponseBody(response);
	}

	
	/**
	 * Find All Bond Holders
	 * @param businessUnit
	 * @param transitMode
	 * @return
	 */
	@HystrixCommand
	public List<BondHolder> findBondHoldersByCriteria(String businessUnit, String transitMode) {
		log.debug("Reference data client method to find all bond holders based on the business unit and transit mode");
		Map<String, String> parameter = new HashMap<>();
		parameter.put("businessUnit", businessUnit);
		parameter.put("transitMode", transitMode);
		ResponseEntity<List<BondHolder>> response = referenceDataRestTemplate.exchange(
				referenceDataProperties.getBaseURL()
						+ "/bondholder/search/findbondholdersbycriteria?businessUnit={businessUnit}&transitMode={transitMode}",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<BondHolder>>() {
				}, parameter);

		return getResponseBodyList(response);
	}
	
	
	/**
	 * @param serviceTypeCode
	 * @return
	 */
	public Resources<BondHolder> findByBondHolderCode(String serviceTypeCode) {
		log.debug(
				"Entered Reference data client method to find whether the bond holder code passed is available / not in the Database");
		Map<String, Object> parameter = new HashMap<>();
		parameter.put("bondHolderCode", serviceTypeCode);
		ResponseEntity<Resources<BondHolder>> response = referenceDataRestTemplate.exchange(
				referenceDataProperties.getBaseURL() + "/Bondholders/{bondHolderCode}", HttpMethod.GET, null,
				new ParameterizedTypeReference<Resources<BondHolder>>() {
				}, parameter);
		return getResponseBody(response);
	}

	/**
	 * @param bondTypeCode
	 * @return
	 */
	public Resources<BondType> findByBondTypeCode(String bondTypeCode) {
		log.debug(
				"Entered Reference data client method to find whether the bond type code is available / not in the Database");
		Map<String, Object> parameter = new HashMap<>();
		parameter.put("bondTypeCode", bondTypeCode);
		ResponseEntity<Resources<BondType>> response = referenceDataRestTemplate.exchange(
				referenceDataProperties.getBaseURL() + "/Bondtypes/{bondTypeCode}", HttpMethod.GET, null,
				new ParameterizedTypeReference<Resources<BondType>>() {
				}, parameter);
		return getResponseBody(response);
	}
	
	/**
	 * @param commentTypeCode
	 * @return
	 */
	public Resources<CommentType> findByCommentTypeCode(String commentTypeCode) {
		log.debug(
				"Entered Reference data client method to find whether the bond type code is available / not in the Database");
		Map<String, Object> parameter = new HashMap<>();
		parameter.put("commentTypeCode", commentTypeCode);
		ResponseEntity<Resources<CommentType>> response = referenceDataRestTemplate.exchange(
				referenceDataProperties.getBaseURL() + "/commenttypes/{commentTypeCode}", HttpMethod.GET, null,
				new ParameterizedTypeReference<Resources<CommentType>>() {
				}, parameter);
		return getResponseBody(response);
	}
	/**
	 * @param stopReasonCode
	 * @return
	 */
	public Resources<StopReason> findByStopReasonCode(String stopReasonCode) {
		log.debug(
				"Entered Reference data client method to find whether the stop reason code is available / not in the Database");
		Map<String, Object> parameter = new HashMap<>();
		parameter.put("stopReasonCode", stopReasonCode);
		ResponseEntity<Resources<StopReason>> response = referenceDataRestTemplate.exchange(
				referenceDataProperties.getBaseURL() + "/commenttypes/{stopReasonCode}", HttpMethod.GET, null,
				new ParameterizedTypeReference<Resources<StopReason>>() {
				}, parameter);
		return getResponseBody(response);
	}
	
	/**
	 * @param orderChannelCode
	 * @return
	 */
	public Resources<OrderChannel> findByOrderChannelCode(String orderChannelCode) {
		log.debug(
				"Entered Reference data client method to find whether the Order channel code is available / not in the Database");
		Map<String, Object> parameter = new HashMap<>();
		parameter.put("orderChannelCode", orderChannelCode);
		ResponseEntity<Resources<OrderChannel>> response = referenceDataRestTemplate.exchange(
				referenceDataProperties.getBaseURL() + "/orderchannels/{orderChannelCode}", HttpMethod.GET, null,
				new ParameterizedTypeReference<Resources<OrderChannel>>() {
				}, parameter);
		return getResponseBody(response);
	}
	
	/**
	 * @param requestedAppointmentTypeCode
	 * @return
	 */
	public Resources<RequestedAppointmentType> findByRequestedAppointmentTypeCode(String requestedAppointmentTypeCode) {
		log.debug(
				"Entered Reference data client method to find whether the Requested Appointment Type code is available / not in the Database");
		Map<String, Object> parameter = new HashMap<>();
		parameter.put("requestedAppointmentTypeCode", requestedAppointmentTypeCode);
		ResponseEntity<Resources<RequestedAppointmentType>> response = referenceDataRestTemplate.exchange(
				referenceDataProperties.getBaseURL() + "/requestedappointmenttypes/{requestedAppointmentTypeCode}", HttpMethod.GET, null,
				new ParameterizedTypeReference<Resources<RequestedAppointmentType>>() {
				}, parameter);
		return getResponseBody(response);
	}
	
	/**
	 * @param serviceTypeCode
	 * @return
	 */
	public Resources<Service> findByServiceTypeCode(String serviceTypeCode) {
		log.debug(
				"Entered Reference data client method to find whether the Service type code is available / not in the Database");
		Map<String, Object> parameter = new HashMap<>();
		parameter.put("serviceTypeCode", serviceTypeCode);
		ResponseEntity<Resources<Service>> response = referenceDataRestTemplate.exchange(
				referenceDataProperties.getBaseURL() + "/requestedappointmenttypes/{requestedAppointmentTypeCode}", HttpMethod.GET, null,
				new ParameterizedTypeReference<Resources<Service>>() {
				}, parameter);
		return getResponseBody(response);
	}
	
	/**
	 * @param appointmentInstructionID
	 * @return
	 */
	public Resources<AppointmentInstruction> findByAppointmentInstructionID(Integer appointmentInstructionID) {
		log.debug(
				"Entered Reference data client method to find whether the appointment Instruction ID is available / not in the Database");
		Map<String, Object> parameter = new HashMap<>();
		parameter.put("appointmentInstructionID", appointmentInstructionID);
		ResponseEntity<Resources<AppointmentInstruction>> response = referenceDataRestTemplate.exchange(
				referenceDataProperties.getBaseURL() + "/appointmentinstructiontexts/{appointmentInstructionID}", HttpMethod.GET, null,
				new ParameterizedTypeReference<Resources<AppointmentInstruction>>() {
				}, parameter);
		return getResponseBody(response);
	}
	
	/**
	 * @param appointmentInstructionID
	 * @return
	 */
	public Resources<AppointmentInstruction> findAppointmentInstructions() {
		log.debug(
				"Load Appointment Instructions in dropdown");
		ResponseEntity<Resources<AppointmentInstruction>> response = referenceDataRestTemplate.exchange(
				referenceDataProperties.getBaseURL() + "/appointmentInstructions", HttpMethod.GET, null,
				new ParameterizedTypeReference<Resources<AppointmentInstruction>>() {
				});
		return getResponseBody(response);
	}
	
	/**
	 * @return Requested Appointment Type
	 */
	public Resources<RequestedAppointmentType> findAllRequestedAppointmentType()
	{
		log.debug("Entered Reference data client method to fetch All Requested Appointment Types");
		ResponseEntity<Resources<RequestedAppointmentType>> response = referenceDataRestTemplate.exchange(
				referenceDataProperties.getBaseURL() + "/requestedAppointmentTypes", HttpMethod.GET, null,
				new ParameterizedTypeReference<Resources<RequestedAppointmentType>>() {
				});
		return getResponseBody(response);
	}	
	/**
	 * Find all stops
	 * 
	 * @return
	 */
	@HystrixCommand
	public Resources<StopReason> findAllStopReasons() {
		log.debug("Order Client method to find All Stops");
		ResponseEntity<Resources<StopReason>> response = referenceDataRestTemplate.exchange(
				referenceDataProperties.getBaseURL() + "/stopReasons", HttpMethod.GET, null,
				new ParameterizedTypeReference<Resources<StopReason>>() {
				});
		return getResponseBodyWithResources(response);

	}

	/**
	 * To Find the bond types
	 * moved from Order client
	 * @return
	 */
	@HystrixCommand
	public Resources<BondType> getBondTypes() {
		log.debug("Order Client method to find all Bond Types");
		ResponseEntity<Resources<BondType>> response = referenceDataRestTemplate.exchange(
				referenceDataProperties.getBaseURL() + "/bondTypes", HttpMethod.GET, null,
				new ParameterizedTypeReference<Resources<BondType>>() {
				});
		return getResponseBodyWithResources(response);

	}
	
	/**
	 * Find All Order Channel
	 * 
	 * @return
	 */
	@HystrixCommand
	public Resources<OrderChannel> findOrderChannel() {
		log.debug("Order Client method to find all Order Channel");
		ResponseEntity<Resources<OrderChannel>> response = referenceDataRestTemplate.exchange(
				referenceDataProperties.getBaseURL() + "/orderChannels", HttpMethod.GET, null,
				new ParameterizedTypeReference<Resources<OrderChannel>>() {
				});
		return getResponseBodyWithResources(response);

	}
	
}