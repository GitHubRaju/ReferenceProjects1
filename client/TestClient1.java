

import static com.test.boot.utility.ClientUtility.getResponseBody;
import static com.test.boot.utility.ClientUtility.getResponseBodyAsList;
import static com.test.boot.utility.ClientUtility.getResponseBodyAsString;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;

/**
 * Client for .
 *Spring's client abstraction to deal with Rest based services is RestTemplate, and this can deal with a variety of message formats(xml, json, atom etc) using an abstraction called HttpMessageConverter to deal with the specifics of binding for each of the message formats.

*Spring RestTemplate provides its own implementation of Super Type token to be able to bind different message formats to parameterized types, along the lines of Jackson's TypeReference, it is called the ParameterizedTypeReference. 

*ParameterizedTypeReference can be used to cleanly bind Rest responses for User and Customer to Java types 
*ParameterizedTypeReference provides a neat way of dealing with the parameterized types and is incredibly useful in consuming the Spring Hateoas based REST services.

*RestTemplate restTemplate = new RestTemplate();
*ResponseEntity<Resource<User>> responseEntity =
* restTemplate.exchange("http://localhost:8080/users/2", HttpMethod.GET, null, new ParameterizedTypeReference<Resource<User>>() {}, Collections.emptyMap());
*if (responseEntity.getStatusCode() == HttpStatus.OK) {
* Resource<User> userResource = responseEntity.getBody();
* User user = userResource.getContent();
*}

*RestTemplate restTemplate = new RestTemplate();
*ResponseEntity<Resource<Customer>> responseEntity =
*  restTemplate.exchange("http://localhost:8080/users/2/customers/17", HttpMethod.GET, null, new ParameterizedTypeReference<Resource<Customer>>() {}, Collections.emptyMap());
*if (responseEntity.getStatusCode() == HttpStatus.OK) {
* Resource<Customer> customerResource = responseEntity.getBody();
* Customer customer = customerResource.getContent();
*}
 */
@Slf4j
@Component
public class OrderClient {

	private static final String PROFILE_CLIENT_METHOD_TO_ADD_CONTACTS = "Profile Client method to add contacts";

	private static final String ORDER_ID = "orderId";

	@Autowired
	private OrderProperties orderProperties;

	@Autowired
	@Qualifier("orderRestTemplate")
	private RestTemplate orderRestTemplate;

	/**
	 * @param orderId
	 * @return
	 */
	@HystrixCommand
	public List<ChargeDTO> findChargesByOrderId(int orderId) {
		log.debug("order client method to find all charges");
		Map<String, Integer> parameter = new HashMap<>();
		parameter.put(ORDER_ID, orderId);
		ParameterizedTypeReference<List<ChargeDTO>> responseType = new ParameterizedTypeReference<List<ChargeDTO>>() {
		};
		ResponseEntity<List<ChargeDTO>> response = orderRestTemplate.exchange(
				orderProperties.getBaseURL() + "/orders/{orderId}/charges", HttpMethod.GET, null, responseType,
				parameter);

		return getResponseBodyAsList(response);
	}

	/**
	 * 
	 * @param orderId
	 * @return List<CommentDTO>
	 */
	@HystrixCommand
	public List<CommentDTO> findCommentsByOrderId(int orderId) {
		log.debug("order client method to find all Comments");
		Map<String, Integer> parameter = new HashMap<>();
		parameter.put(ORDER_ID, orderId);
		ParameterizedTypeReference<List<CommentDTO>> responseType = new ParameterizedTypeReference<List<CommentDTO>>() {
		};
		ResponseEntity<List<CommentDTO>> response = orderRestTemplate.exchange(
				orderProperties.getBaseURL() + "/orders/{orderId}/comments", HttpMethod.GET, null, responseType,
				parameter);

		return getResponseBodyAsList(response);
	}

	/**
	 * 
	 * @param orderId
	 * @return
	 */
	@HystrixCommand
	public List<ReferenceNumberDTO> findReferenceNumbersByOrderId(int orderId) {
		log.debug("order client method to find all reference numbers");
		Map<String, Integer> parameter = new HashMap<>();
		parameter.put(ORDER_ID, orderId);
		ParameterizedTypeReference<List<ReferenceNumberDTO>> responseType = new ParameterizedTypeReference<List<ReferenceNumberDTO>>() {
		};
		ResponseEntity<List<ReferenceNumberDTO>> response = orderRestTemplate.exchange(
				orderProperties.getBaseURL() + "/orders/{orderId}/referencenumbers", HttpMethod.GET, null, responseType,
				parameter);

		return getResponseBodyAsList(response);
	}

	/**
	 * @param trailerPrefix
	 * @param trailerNumber
	 * @return
	 */
	@HystrixCommand
	public EquipmentType validateTrailerNumber(String trailerPrefix, String trailerNumber) {
		log.debug("Order Client method to validate Trailer Number");
		Map<String, String> parameter = new HashMap<>();
		parameter.put("trailerprefix", trailerPrefix);
		parameter.put("trailernumber", trailerNumber);
		ResponseEntity<EquipmentType> response = orderRestTemplate.exchange(
				orderProperties.getBaseURL() + "/equipments/validatecompanytrailer/{trailerprefix}/{trailernumber}",
				HttpMethod.GET, null, new ParameterizedTypeReference<EquipmentType>() {
				}, parameter);
		return getResponseBody(response);
	}

	/**
	 * Find all stop items based on stopId & orderId
	 * 
	 * @param orderId
	 * @param stopId
	 * @return List<StopItem>
	 */
	@HystrixCommand

	public List<StopItem> findItemsByOrderIdAndStopId(Integer orderId, Integer stopId) {
		log.debug("Order Client method to find all Stop items by stop Id " + stopId + " & order Id" + orderId);
		Map<String, Integer> parameter = new HashMap<>();
		parameter.put(ORDER_ID, orderId);
		parameter.put("stopId", stopId);
		ParameterizedTypeReference<List<StopItem>> responseType = new ParameterizedTypeReference<List<StopItem>>() {
		};
		ResponseEntity<List<StopItem>> response = orderRestTemplate.exchange(
				orderProperties.getBaseURL() + "/orders/{orderId}/stops/{stopId}/items", HttpMethod.GET, null,
				responseType, parameter);

		return getResponseBodyAsList(response);
	}

	/**
	 * @param trailerPrefix
	 * @param trailerNumber
	 * @return
	 */
	@HystrixCommand
	public String getTrailerAvailabiltyStatus(String trailerPrefix, String trailerNumber) {
		log.debug("Order client method to validate trailer Availablity");
		Map<String, String> parameter = new HashMap<>();
		parameter.put("trailerPrefix", trailerPrefix);
		parameter.put("trailerNumber", trailerNumber);
		ResponseEntity<String> response = orderRestTemplate.exchange(
				orderProperties.getBaseURL() + "/equipments/traileravailabilitystatus/{trailerPrefix}/{trailerNumber}",
				HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
				}, parameter);
		return getResponseBodyAsString(response);
	}

	/**
	 * @param order
	 * @return
	 */
	@HystrixCommand
	public OrderResponseDTO orderCreation(Order order) {
		log.debug("order client to create order");
		HttpEntity<Order> httpEntity = new HttpEntity<>(order);

		ResponseEntity<OrderResponseDTO> response = orderRestTemplate.exchange(
				orderProperties.getBaseURL() + "/orders/ordercreation", HttpMethod.POST, httpEntity,
				new ParameterizedTypeReference<OrderResponseDTO>() {
				});
		return getResponseBody(response);
	}

	/**
	 * @param order
	 * @return
	 */
	@HystrixCommand
	public Order createOrder(Order order) {
		log.debug("order client to insert orders");
		HttpEntity<Order> httpEntity = new HttpEntity<>(order);
		ResponseEntity<Order> response = orderRestTemplate.exchange(orderProperties.getBaseURL() + "/orders",
				HttpMethod.POST, httpEntity, new ParameterizedTypeReference<Order>() {
				});
		return getResponseBody(response);
	}

	/**
	 * @param order
	 * @return
	 */
	@HystrixCommand
	public OrderAssociatedParty createAdditionalParty() {
		log.debug("order client to insert additional orders");
		OrderAssociatedParty orderAssociatedParty2 = new OrderAssociatedParty();
		orderAssociatedParty2.setPartyCode("PC012");
		orderAssociatedParty2.setPartyRoleCode("RC01");
		HttpEntity<OrderAssociatedParty> httpEntity = new HttpEntity<>(orderAssociatedParty2);
		ResponseEntity<OrderAssociatedParty> response = orderRestTemplate.exchange(
				orderProperties.getBaseURL() + "/orderAssociatedParties", HttpMethod.POST, httpEntity,
				new ParameterizedTypeReference<OrderAssociatedParty>() {
				});
		return getResponseBody(response);
	}

	/**
	 * @param orderAssociatedParty
	 * @return
	 */
	@HystrixCommand
	public OrderAssociatedParty saveOrderAssociatedParty(String orderAssociatedParty) {
		log.debug("Profile Client method to add Order Associated Party");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(orderAssociatedParty, headers);
		ResponseEntity<OrderAssociatedParty> response = orderRestTemplate.exchange(
				orderProperties.getBaseURL() + "/orderAssociatedParties", HttpMethod.POST, entity,
				OrderAssociatedParty.class, headers);
		return getResponseBody(response);
	}

	/**
	 * @param orderEquipmentRequirementFeatureAssociation
	 * @return
	 */
	@HystrixCommand
	public String saveOrderEquipmentRequirementFeatureAssociation(String orderEquipmentRequirementFeatureAssociation) {
		log.debug(PROFILE_CLIENT_METHOD_TO_ADD_CONTACTS);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(orderEquipmentRequirementFeatureAssociation, headers);
		ResponseEntity<String> response = (ResponseEntity<String>) orderRestTemplate.exchange(
				orderProperties.getBaseURL() + "/orderEquipmentRequirementFeatureAssociations", HttpMethod.POST, entity,
				String.class);
		return getResponseBody(response);
	}

	/**
	 * @param orderEquipmentRequirementSpecificationAssociation
	 * @return
	 */
	@HystrixCommand
	public String saveOrderEquipmentRequirementSpecificationAssociation(
			String orderEquipmentRequirementSpecificationAssociation) {
		log.debug(PROFILE_CLIENT_METHOD_TO_ADD_CONTACTS);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(orderEquipmentRequirementSpecificationAssociation, headers);
		ResponseEntity<String> response = (ResponseEntity<String>) orderRestTemplate.exchange(
				orderProperties.getBaseURL() + "/orderEquipmentRequirementSpecificationAssociations", HttpMethod.POST,
				entity, String.class);
		return getResponseBody(response);
	}

	/**
	 * @param orderEquipmentRequirementSpecificationDetail
	 * @return
	 */
	@HystrixCommand
	public String saveOrderEquipmentRequirementSpecificationDetail(
			String orderEquipmentRequirementSpecificationDetail) {
		log.debug(PROFILE_CLIENT_METHOD_TO_ADD_CONTACTS);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(orderEquipmentRequirementSpecificationDetail, headers);
		ResponseEntity<String> response = (ResponseEntity<String>) orderRestTemplate.exchange(
				orderProperties.getBaseURL() + "/orderEquipmentRequirementSpecificationDetails", HttpMethod.POST,
				entity, String.class);
		return getResponseBody(response);
	}

	/**
	 * @param orderCrossBorderDetail
	 * @return
	 */
	@HystrixCommand
	public String saveOrderCrossBorderDetail(String orderCrossBorderDetail) {
		log.debug(PROFILE_CLIENT_METHOD_TO_ADD_CONTACTS);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(orderCrossBorderDetail, headers);
		ResponseEntity<String> response = (ResponseEntity<String>) orderRestTemplate.exchange(
				orderProperties.getBaseURL() + "/orderCrossBorderDetails", HttpMethod.POST, entity, String.class);
		return getResponseBody(response);
	}

	/**
	 * @param orderService
	 * @return
	 */
	@HystrixCommand
	public String saveOrderService(String orderService) {
		log.debug(PROFILE_CLIENT_METHOD_TO_ADD_CONTACTS);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(orderService, headers);
		ResponseEntity<String> response = (ResponseEntity<String>) orderRestTemplate
				.exchange(orderProperties.getBaseURL() + "/orderServices", HttpMethod.POST, entity, String.class);
		return getResponseBody(response);
	}

	/**
	 * @param orderCharge
	 * @return
	 */
	@HystrixCommand
	public String saveOrderCharge(String orderCharge) {
		log.debug(PROFILE_CLIENT_METHOD_TO_ADD_CONTACTS);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(orderCharge, headers);
		ResponseEntity<String> response = (ResponseEntity<String>) orderRestTemplate
				.exchange(orderProperties.getBaseURL() + "/orderCharges", HttpMethod.POST, entity, String.class);
		return getResponseBody(response);
	}

	/**
	 * @param orderId
	 * @return
	 */
	@HystrixCommand
	public OrderDTO getOrderByOrderID(Integer orderId) {
		log.debug("Order client method to get orderdto based on order id");
		Map<String, Integer> parameter = new HashMap<>();
		parameter.put(ORDER_ID, orderId);
		ResponseEntity<OrderDTO> response = orderRestTemplate.exchange(
				orderProperties.getBaseURL() + "/orders/{orderId}/copyorder", HttpMethod.GET, null,
				new ParameterizedTypeReference<OrderDTO>() {
				}, parameter);
		return getResponseBody(response);
	}

	/**
	 * @param orderEquipmentRequirement
	 * @return
	 */
	@HystrixCommand
	public String saveEquipment(String orderEquipmentRequirement) {
		log.debug("Order Client:Client for order Equipment save");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(orderEquipmentRequirement, headers);
		ResponseEntity<String> response = (ResponseEntity<String>) orderRestTemplate.exchange(
				orderProperties.getBaseURL() + "/orderEquipmentRequirements", HttpMethod.POST, entity, String.class);
		return getResponseBody(response);
	}

	/**
	 * @param comment
	 * @return
	 */
	@HystrixCommand
	public String saveOrderComment(String comment) {
		log.debug("Order Client:Client for order comment save");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(comment, headers);
		ResponseEntity<String> response = (ResponseEntity<String>) orderRestTemplate
				.exchange(orderProperties.getBaseURL() + "/orderComments", HttpMethod.POST, entity, String.class);
		return getResponseBody(response);
	}

	/**
	 * @param orderReferenceNumber
	 * @return
	 */
	@HystrixCommand
	public String saveOrderReference(String orderReferenceNumber) {
		log.debug("Order Client:Client for order reference save");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(orderReferenceNumber, headers);
		ResponseEntity<String> response = (ResponseEntity<String>) orderRestTemplate.exchange(
				orderProperties.getBaseURL() + "/orderReferenceNumbers", HttpMethod.POST, entity, String.class);
		return getResponseBody(response);
	}

	/**
	 * @param stop
	 * @return
	 */
	@HystrixCommand
	public String saveStop(String stop) {
		log.debug("Order Client:Client for Stop save");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(stop, headers);
		ResponseEntity<String> response = (ResponseEntity<String>) orderRestTemplate
				.exchange(orderProperties.getBaseURL() + "/stops", HttpMethod.POST, entity, String.class);
		return getResponseBody(response);
	}

	/**
	 * @param charge
	 * @return
	 */
	@HystrixCommand
	public String saveStopCharges(String charge) {
		log.debug("Order Client:Client for stop charge save");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(charge, headers);
		ResponseEntity<String> response = (ResponseEntity<String>) orderRestTemplate
				.exchange(orderProperties.getBaseURL() + "/stopCharges", HttpMethod.POST, entity, String.class);
		return getResponseBody(response);
	}

	/**
	 * @param stopComment
	 * @return
	 */
	@HystrixCommand
	public String saveStopComment(String stopComment) {
		log.debug("Order Client:Client for stop comment save");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(stopComment, headers);
		ResponseEntity<String> response = (ResponseEntity<String>) orderRestTemplate
				.exchange(orderProperties.getBaseURL() + "/stopComments", HttpMethod.POST, entity, String.class);
		return getResponseBody(response);
	}

	/**
	 * @param stopReference
	 * @return
	 */
	@HystrixCommand
	public String saveStopReference(String stopReference) {
		log.debug("Order Client:Client for stop reference save");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(stopReference, headers);
		ResponseEntity<String> response = (ResponseEntity<String>) orderRestTemplate.exchange(
				orderProperties.getBaseURL() + "/stopReferenceNumbers", HttpMethod.POST, entity, String.class);
		return getResponseBody(response);
	}
	
	
	
	/**
	 * @param orderMaterialHandlingRequirementAssociation
	 * @return
	 */
	@HystrixCommand
	public String saveOrderMaterialHandling(String orderMaterialHandlingRequirementAssociation) {
		log.debug("Order Client:Client for order material handling save");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(orderMaterialHandlingRequirementAssociation, headers);
		ResponseEntity<String> response = (ResponseEntity<String>) orderRestTemplate.exchange(
				orderProperties.getBaseURL() + "/orderMaterialHandlingRequirementAssociations", HttpMethod.POST, entity, String.class);
		return getResponseBody(response);
	}
	
	
	/**
	 * @param stopService
	 * @return
	 */
	@HystrixCommand
	public String saveStopService(String stopService) {
		log.debug("Order Client:Client for stop service save");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(stopService, headers);
		ResponseEntity<String> response = (ResponseEntity<String>) orderRestTemplate
				.exchange(orderProperties.getBaseURL() + "/stopServices", HttpMethod.POST, entity, String.class);
		return getResponseBody(response);
	}
	
	/**
	 * @param requestedAppointmentInstructionAssociation
	 * @return
	 */
	@HystrixCommand
	public String saveRequestedAppointmentInstructionAssociation(String requestedAppointmentInstructionAssociation) {
		log.debug("Order Client:Client for requested Appointment Instruction Association save");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(requestedAppointmentInstructionAssociation, headers);
		ResponseEntity<String> response = (ResponseEntity<String>) orderRestTemplate
				.exchange(orderProperties.getBaseURL() + "/requestedAppointmentInstructionAssociations", HttpMethod.POST, entity, String.class);
		return getResponseBody(response);
	}
	
	/**
	 * 
	 * @param orderId
	 * @return OrderUnifiedCustomerRequestAssociation
	 */
	@HystrixCommand
	public OrderUnifiedCustomerRequestAssociation findLatestUnifiedCustomerRequest(Integer orderId) {
		log.debug("Order Client method to findLatestUnifiedCustomerRequest by orderid");
		OrderUnifiedCustomerRequestAssociation orderUnifiedCustomerRequestAssociation = null;
		Map<String, Integer> parameter = new HashMap<>();
		parameter.put("orderId", orderId);
		ResponseEntity<Resources<OrderUnifiedCustomerRequestAssociation>> response = orderRestTemplate.exchange(
				orderProperties.getBaseURL()
						+ "/orderUnifiedCustomerRequestAssociations?order.orderID={orderId}&sort=unifiedCustomerRequestID,desc",
				HttpMethod.GET, null, new ParameterizedTypeReference<Resources<OrderUnifiedCustomerRequestAssociation>>() {
				}, parameter);
		Collection<OrderUnifiedCustomerRequestAssociation> orderUnifiedCustomerRequestAssociations = response.getBody().getContent();
		if (Optional.ofNullable(orderUnifiedCustomerRequestAssociations).isPresent()) {
			orderUnifiedCustomerRequestAssociation = orderUnifiedCustomerRequestAssociations.stream().findFirst().get();
		}
		return getResponseBody(orderUnifiedCustomerRequestAssociation);
	}

}
