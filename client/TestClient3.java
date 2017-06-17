

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;

/**
 * Client for 
 *
 */
@Slf4j
@Component
public class ProfileClient {
	public static final String BILLTOCODE = "billtocode";
	public static final String LOCATIONCODE = "locationcode";
	public static final String ROLETYPE ="roletype";
	public static final String ACTIVE ="active";
//location
	@Autowired
	private ProfileProperties profileProperties;

	@Autowired
	@Qualifier("profileRestTemplate")
	private RestTemplate profileRestTemplate;

	/**
	 * To find Time zone by location code
	 * 
	 * @param locationcode
	 * @return
	 */
	@HystrixCommand
	public Map<String, String> findTimeZoneByLocationCode(String locationcode) {
		log.debug("profile client method to find time zone by location code");
		Map<String, String> parameter = new HashMap<>();
		parameter.put(LOCATIONCODE, locationcode);
		ResponseEntity<Map<String, String>> response = profileRestTemplate.exchange(
				profileProperties.getLocationURL() + "/timezone/search/findbylocationcode/{locationcode}", HttpMethod.GET,
				null, new ParameterizedTypeReference<Map<String, String>>() {
				}, parameter);
		return getResponseBody(response);
	}

	/**
	 * To validate Marketing area
	 * 
	 * @param marketingarea
	 * @return
	 */
	@HystrixCommand
	public Map<String, Boolean> validateMarketingArea(String marketingarea) {
		log.debug("profile client method to validate Marketing areas");
		Map<String, String> parameter = new HashMap<>();
		parameter.put("marketingarea", marketingarea);
		ResponseEntity<Map<String, Boolean>> response = profileRestTemplate.exchange(
				profileProperties.getLocationURL() + "/locations/validatemarketingarea/{marketingarea}", HttpMethod.GET,
				null, new ParameterizedTypeReference<Map<String, Boolean>>() {
				}, parameter);

		return getResponseBody(response);
	}

	/**
	 * To find all clearing Countries
	 * 
	 * @return
	 */
	@HystrixCommand
	public Map<String, List<CountryDTO>> findAllClearingCountries() {
		log.debug("profile client method for finding clearing countries");
		ResponseEntity<Map<String, List<CountryDTO>>> response = profileRestTemplate.exchange(
				profileProperties.getCustomerURL() + "/clearingcountries", HttpMethod.GET, null,
				new ParameterizedTypeReference<Map<String, List<CountryDTO>>>() {
				}, Collections.emptyMap());
		return getResponseBody(response);
	}

	/**
	 * To find auto rate status by bill to code
	 * @param billtocode
	 * @return
	 */
	@HystrixCommand
	public Map<String, Boolean> findAutoRateStatusByBillToCode(String billtocode) {

		log.debug("Profile client method for Finding Auto Rate Status By Billtocode");
		Map<String, String> parameter = new HashMap<>();
		parameter.put(BILLTOCODE, billtocode);
		ResponseEntity<Map<String, Boolean>> response = profileRestTemplate.exchange(
				profileProperties.getCustomerURL() + "/profiles/autorate/search/findbybillingparty/{billtocode}",
				HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Boolean>>() {
				}, parameter);
		return getResponseBody(response);
	}

	/**
	 * To find the primary reference number by bill to code
	 * @param billtocode
	 * @return
	 */
	@HystrixCommand
	public CustomerProfileDTO findPrimaryReferenceNumberByBillToCode(Integer billtoID) {
		log.debug("Profile client method for Finding Primary Reference Number By Bill To code");
		Map<String, String> parameter = new HashMap<>();
		parameter.put(BILLTOCODE, billtoID.toString());
		ResponseEntity<CustomerProfileDTO> response = profileRestTemplate.exchange(
				profileProperties.getCustomerURL() + "/profiles/primaryreference/search/findbybillingparty/{billtocode}",
				HttpMethod.GET, null, new ParameterizedTypeReference<CustomerProfileDTO>() {
				}, parameter);
		return getResponseBody(response);
	}

	/**
	 * To find facility overview requirement by location code
	 * @param locationcode
	 * @return
	 */
	@HystrixCommand
	public List<FacilityOverviewRequirementDTO> findFacilityOverviewRequirementByLocationCode(Integer locationID) {
		log.debug("Profile client method for finding facility overview requirement by location code");
		Map<String, String> parameter = new HashMap<>();
		parameter.put(LOCATIONCODE, locationID.toString());
		ResponseEntity<List<FacilityOverviewRequirementDTO>> response = profileRestTemplate.exchange(
				profileProperties.getLocationURL() + "/facilityoverviewrequirements/{locationcode}", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<FacilityOverviewRequirementDTO>>() {
				}, parameter);

		return getResponseBody(response);
	}

	/**
	 * To find Contact methods
	 * @return
	 */
	@HystrixCommand
	public List<ContactMethodDTO> findAllContactMethods() {
		log.debug("Fetching the contact method of the user");
		ResponseEntity<List<ContactMethodDTO>> response = profileRestTemplate.exchange(
				profileProperties.getCustomerURL() + "/contactmethods", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<ContactMethodDTO>>() {
				}, Collections.emptyMap());
		return getResponseBody(response);
	}

	/**
	 * To find rating cycle code by bill to code
	 * @param billtocode
	 * @return
	 */
	@HystrixCommand
	public RatingCycleDTO findRatingCycleCodeByBillCode(String billtocode) {
		log.debug("Fetching the rating cycle code by bill to code");
		Map<String, String> parameter = new HashMap<>();
		parameter.put(BILLTOCODE, billtocode);
		ResponseEntity<RatingCycleDTO> response = profileRestTemplate.exchange(
				profileProperties.getCustomerURL() + "/profiles/ratingcyclecode/search/findbybillingparty/{billtocode}",
				HttpMethod.GET, null, new ParameterizedTypeReference<RatingCycleDTO>() {
				}, parameter);
		return getResponseBody(response);
	}

	/**
	 * To Find out sourcing flag By solicitor code
	 * @param solicitorcode
	 * @return
	 */
	@HystrixCommand
	public Map<String, Boolean> findOutSourcingFlagBySolicitorCode(String solicitorcode) {
		log.debug("Profile client method for Finding outsourcing flag By solicitorcode");
		Map<String, String> parameter = new HashMap<>();
		parameter.put("solicitorcode", solicitorcode);
		ResponseEntity<Map<String, Boolean>> response = profileRestTemplate.exchange(
				profileProperties.getCustomerURL() + "/profiles/outsourcing/search/findbysolicitor/{solicitorcode}",
				HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Boolean>>() {
				}, parameter);
		return getResponseBody(response);
	}


	

	/**
	 * To find customer details associated to bill to customer
	 * @param billtocode
	 * @return
	 */
	@HystrixCommand
	public CustomerDTO findAllCustomerDetailsByBillToCode(String billtocode) {
		log.debug("Profile Client method to get customer details associated to bill to customer");
		Map<String, String> parameter = new HashMap<>();
		parameter.put(BILLTOCODE, billtocode);
		ResponseEntity<CustomerDTO> response = profileRestTemplate.exchange(
				profileProperties.getCustomerURL() + "/customers/search/findbybillingparty/{billtocode}", HttpMethod.GET,
				null, new ParameterizedTypeReference<CustomerDTO>() {
				}, parameter);
		return getResponseBody(response);
	}

	/**
	 * To find all contact titles
	 * @return
	 */
	@HystrixCommand
	public List<TitleDTO> findAllContactTitles() {
		log.debug("Profile Client method to find all contact titles");
		ResponseEntity<List<TitleDTO>> response = profileRestTemplate.exchange(
				profileProperties.getCustomerURL() + "/contacttitles", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<TitleDTO>>() {
				}, Collections.emptyMap());
		return getResponseBody(response);

	}

	/**
	 * To add contacts
	 * @param profileContactDTO
	 * @return
	 */
	@HystrixCommand
	public String saveContact(ProfileContactDTO profileContactDTO) {
		log.debug("Profile Client method to add contacts");
		Map<String, ProfileContactDTO> parameter = new HashMap<>();
		parameter.put("ProfileContactDTO", profileContactDTO);
		String response=profileRestTemplate.postForObject(profileProperties.getLocationURL() + "/contacts", profileContactDTO,
				String.class);
		return getResponseBody(response);
	}

	/**
	 * To add general instructions
	 * @param locationProfileDTO
	 * @return
	 */
	@HystrixCommand
	public String saveGeneralInstruction(LocationProfileDTO locationProfileDTO) {
		log.debug("Profile client method to add general instructions");

		Map<String, LocationProfileDTO> parameter = new HashMap<>();
		parameter.put("LocationProfileDTO", locationProfileDTO);
		String response=profileRestTemplate.postForObject(
				profileProperties.getLocationURL() + "/generalinstructions",
				locationProfileDTO, String.class);
		return getResponseBody(response);
	}

	/**
	 * To add customer directions 
	 * @param locationProfileDTO
	 * @return
	 */
	@HystrixCommand
	public String saveCustomerDirection(LocationProfileDTO locationProfileDTO) {
		log.debug("Profile client method to add customer directions");

		Map<String, LocationProfileDTO> parameter = new HashMap<>();
		parameter.put("LocationProfileDTO", locationProfileDTO);
		String response=profileRestTemplate.postForObject(profileProperties.getLocationURL() + "/customerdirections",
				locationProfileDTO, String.class);

		return getResponseBody(response);
	}

	/**
	 * To find profile contacts
	 * @param code
	 * @param roletype
	 * @param activestatus
	 * @return
	 */
	@HystrixCommand
	public List<ContactDTO> findAllProfileContacts(ProfileDTO profileDTO) {
		log.debug("Profile client method to find all profile contacts");

		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
				.fromHttpUrl(profileProperties.getCustomerURL() + "/profiles/contacts/search/findbycriteria")
				.queryParam("code", profileDTO.getCode()).queryParam(ROLETYPE, profileDTO.getRoleTye()).queryParam(ACTIVE, profileDTO.getPartyStatus());
		ResponseEntity<List<ContactDTO>> response = profileRestTemplate.exchange(
				uriComponentsBuilder.build().encode().toUri(), HttpMethod.GET, null,
				new ParameterizedTypeReference<List<ContactDTO>>() {
				});
		return getResponseBody(response);

	}

	/**
	 * To validate the equipment specification
	 * 
	 * @param equipmentlength
	 * @param equipmenttype
	 * @param locations
	 * @return
	 * Changed from String[] to Integer[] in arguments- temporary fix - check this again
	 */
    @HystrixCommand
    public Map<String, Boolean> validateEquipmentSpecification(int equipmentlength, String equipmenttype,
                Integer[] locations) {
          log.debug("profile client method for validating location specification");
          UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                      .fromHttpUrl(profileProperties.getLocationURL() + "/equipments/validateequipmentspecification")
                      .queryParam("equipmentlength", equipmentlength).queryParam("equipmenttype", equipmenttype)
                      .queryParam("locations", (Object[]) locations);
          ResponseEntity<Map<String, Boolean>> response = profileRestTemplate.exchange(
                      uriComponentsBuilder.build().encode().toUri(), HttpMethod.GET, null,
                      new ParameterizedTypeReference<Map<String, Boolean>>() {
                      });
          return getResponseBody(response);
    }


	/**
	 * To find the origin/destination location based on zipcode
	 * 
	 * @param zipcode
	 * @param size
	 * @param page
	 * @return
	 */
	@HystrixCommand
	public Map<String, Object> findOriginDestTypeAheadByZipCode(String zipcode, int size, int page) {
		log.debug("profile client to get origin/dest TypeAhead Based On ZipCode");
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
				.fromHttpUrl(profileProperties.getLocationURL() + "/cities/search/typeahead/findbyzipcode")
				.queryParam("zipcode", zipcode).queryParam("size", size).queryParam("page", page);
		ResponseEntity<Map<String, Object>> response = profileRestTemplate.exchange(
				uriComponentsBuilder.build().encode().toUri(), HttpMethod.GET, null,
				new ParameterizedTypeReference<Map<String, Object>>() {
				});
		return getResponseBody(response);
	}

	/**
	 * To find the profile approval status based on billing party
	 * 
	 * @param billtocode
	 * @return
	 */
	@HystrixCommand
	public Map<String, Boolean> findApprovedSiteStatusByBillingParty(Integer billtoID) {
		log.debug("Profile client method to find the profile approval status based on billing party");
		Map<String, String> parameters = new HashMap<>();
		parameters.put(BILLTOCODE, billtoID.toString());
		ResponseEntity<Map<String, Boolean>> response = profileRestTemplate.exchange(
				profileProperties.getCustomerURL() + "/profiles/approvalstatus/{billtocode}",
				HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Boolean>>() {
				}, parameters);
		return getResponseBody(response);
	}

	/**
	 * to find the credit status based on billto code
	 * 
	 * @param billtocode
	 * @return
	 */
	@HystrixCommand
	public Map<String, Boolean> findCreditStatusByBillToCode(Integer billtoID) {
		log.debug("Profile client method to find  the credit status based on billto code ");
		Map<String, String> parameters = new HashMap<>();
		parameters.put(BILLTOCODE, billtoID.toString() );
		ResponseEntity<Map<String, Boolean>> response = profileRestTemplate.exchange(
				profileProperties.getCustomerURL() + "/profiles/creditstatus/nationalaccount/{billtocode}",
				HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Boolean>>() {
				}, parameters);
		return getResponseBody(response);
	}

	/**
	 * to find the location profile based on location code location ID by
	 * location code
	 * 
	 * @param locationcode
	 * @return
	 * Modified Return type of MAp from string to integer
	 */
	@HystrixCommand
	public Map<Integer, List<LocationProfileDTO>> findLocationProfileByLocationCode(Integer locationID) {
		log.debug("Profile client method to find location profile based on location code ");
		Map<String, String> parameters = new HashMap<>();
		parameters.put(LOCATIONCODE, locationID.toString());
		ResponseEntity<Map<Integer, List<LocationProfileDTO>>> response = profileRestTemplate.exchange(
				profileProperties.getLocationURL() + "/locations/search/findbylocationcode/{locationcode}", HttpMethod.GET,
				null, new ParameterizedTypeReference<Map<Integer, List<LocationProfileDTO>>>() {
				}, parameters);
		return getResponseBody(response);

	}

	/**
	 * To find the location profile based on location code with projection
	 * 
	 * @param locationcode
	 * @param projection
	 * @return
	 */
	@HystrixCommand
	public Map<String,String> findLocationProfileByLocationCodeWithProjection(Integer locationID, String projection) {
		log.debug("Profile client method to find location profile based on location code with projection ");
		Map<String, String> parameters = new HashMap<>();
		parameters.put(LOCATIONCODE, locationID.toString());
		parameters.put("projection", projection);
		ResponseEntity<Map<String,String>> response = profileRestTemplate.exchange(
				profileProperties.getLocationURL()
						+ "/locations/search/findbylocationcode/{locationcode}?projection={projection}",
				HttpMethod.GET, null,new ParameterizedTypeReference<Map<String,String>>() {
				}, parameters);
		log.debug("******projection" + projection);
		return getResponseBody(response);
	}

	/**
	 * To find all the exit ports
	 * 
	 * @return
	 */
	@HystrixCommand
	public List<ExitPortDTO> findAllExitPorts() {
		log.debug("Profile client method to find all exit ports");
		ResponseEntity<List<ExitPortDTO>> response = profileRestTemplate.exchange(
				profileProperties.getLocationURL() + "/exitports", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<ExitPortDTO>>() {
				}, Collections.emptyMap());
		return getResponseBody(response);
	}

	/**
	 * To find Entry ports based on exit ports
	 * 
	 * @param exitportcode
	 * @return
	 */
	@HystrixCommand
	public List<EntryPortDTO> findEntryPortsByExitPort(String exitportcode) {
		log.debug("Profile client method to find Entry ports based on exit ports");
		Map<String, String> parameters = new HashMap<>();
		parameters.put("exitportcode", exitportcode);
		ResponseEntity<List<EntryPortDTO>> response = profileRestTemplate.exchange(
				profileProperties.getLocationURL() + "/entryports/search/findbyexitport/{exitportcode}", HttpMethod.GET,
				null, new ParameterizedTypeReference<List<EntryPortDTO>>() {
				}, parameters);
		return getResponseBody(response);
	}

	/**
	 * To find all the Entry ports
	 * 
	 * @return
	 */
	@HystrixCommand
	public List<EntryPortDTO> findAllEntryPorts() {
		log.debug("Profile client method to find  all the Entry ports");
		ResponseEntity<List<EntryPortDTO>> response = profileRestTemplate.exchange(
				profileProperties.getLocationURL() + "/entryports", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<EntryPortDTO>>() {
				}, Collections.emptyMap());
		return getResponseBody(response);
	}

	/**
	 * To find exit ports based on entry ports
	 * 
	 * @param entryportcode
	 * @return
	 */
	@HystrixCommand
	public List<ExitPortDTO> findExitPortsByEntryPort(String entryportcode) {
		log.debug("Profile client method to find exit ports based on entry ports");
		Map<String, String> parameters = new HashMap<>();
		parameters.put("entryportcode", entryportcode);
		ResponseEntity<List<ExitPortDTO>> response = profileRestTemplate.exchange(
				profileProperties.getLocationURL() + "/exitports/search/findbyentryport/{entryportcode}", HttpMethod.GET,
				null, new ParameterizedTypeReference<List<ExitPortDTO>>() {
				}, parameters);
		return getResponseBody(response);
	}

	/**
	 * To find marketing area based on location code
	 * 
	 * @param locationcode
	 * @return
	 */
	@HystrixCommand
	public ContactDTO findMarketingAreaByLocationCode(String locationcode) {
		log.debug("Profile client method to find marketing area based on location code ");
		Map<String, String> parameters = new HashMap<>();
		parameters.put(LOCATIONCODE, locationcode);
		ResponseEntity<ContactDTO> response = profileRestTemplate.exchange(
				profileProperties.getLocationURL() + "/marketingareas/search/findbylocationcode/{locationcode}",
				HttpMethod.GET, null, new ParameterizedTypeReference<ContactDTO>() {
				}, parameters);
		return getResponseBody(response);
	}

	/**
	 * To find the profile based on the criteria mentioned Bill
	 * To/Solicitor/Broker/Shipper/Receiver/Broker Party Type Ahead
	 * 
	 * @param value
	 * @param roletype
	 * @param page
	 * @param size
	 * @param approved
	 * @param addresstype
	 * @param active
	 * @return
	 */
	@HystrixCommand
	public Map<String, Object> findProfileByCriteria(String value, String roletype, int page, int size,
			boolean approved, String addresstype, String active) {
		log.debug("Profile client method to find profile based on the criteria ");
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
				.fromHttpUrl(profileProperties.getCustomerURL() + "/profiles/search/typeahead/findbycriteria")
				.queryParam("value", value).queryParam(ROLETYPE, roletype).queryParam("page", page)
				.queryParam("size", size).queryParam("approved", approved).queryParam("addresstype", addresstype)
				.queryParam(ACTIVE, active);
		ResponseEntity<Map<String, Object>> response = profileRestTemplate.exchange(
				uriComponentsBuilder.build().encode().toUri(), HttpMethod.GET, null,
				new ParameterizedTypeReference<Map<String, Object>>() {
				});
		return getResponseBody(response);
	}

	/**
	 * To find origin destination city based on city
	 * 
	 * @param city
	 * @param size
	 * @param page
	 * @return
	 */
	@HystrixCommand
	public Map<String, Object> findOriginDestCityByCity(String city, int size, int page) {
		log.debug("Profile client method to find origin/destination city based on city");
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
				.fromHttpUrl(profileProperties.getLocationURL() + "/cities/search/typeahead/findbycity")
				.queryParam("city", city).queryParam("size", size).queryParam("page", page);
		ResponseEntity<Map<String, Object>> response = profileRestTemplate.exchange(
				uriComponentsBuilder.build().encode().toUri(), HttpMethod.GET, null,
				new ParameterizedTypeReference<Map<String, Object>>() {
				});
		return getResponseBody(response);
	}

	/**
	 * Search customer details
	 * 
	 * @param code
	 * @param name
	 * @param phoneNumber
	 * @param city
	 * @param state
	 * @param roletype
	 * @param active
	 * @param approved
	 * @param addresstype
	 * @return
	 */
	@HystrixCommand
	public Map<String, ProfileDTO> findCustomerDetails(String code, String name, String phoneNumber, String city,
			String state, String roletype, Boolean active, Boolean approved, String addresstype) {
		log.debug("Profile client  method for searching profile");
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
				.fromHttpUrl(profileProperties.getCustomerURL() + "/profiles/search/findbycriteria")
				.queryParam("code", code).queryParam("name", name).queryParam("phoneNumber", phoneNumber)
				.queryParam("city", city).queryParam("state", state).queryParam(ROLETYPE, roletype)
				.queryParam(ACTIVE, active).queryParam("approved", approved).queryParam("addresstype", addresstype);
		ResponseEntity<Map<String, ProfileDTO>> response = profileRestTemplate.exchange(
				uriComponentsBuilder.build().encode().toUri(), HttpMethod.GET, null,
				new ParameterizedTypeReference<Map<String, ProfileDTO>>() {
				});
		return getResponseBody(response);
	}

	/**
	 * To find List of Profile Details based on party details
	 * 
	 * @param profileRequestDTO
	 * @return
	 * @throws URISyntaxException
	 */
	@HystrixCommand
	public Map<String, List<ProfileResponseDTO>> findProfileDetails(List<ProfileRequestDTO> profileRequestDTO)
			throws URISyntaxException {
		log.debug("profile client method to find List of Profile Details");
		RequestEntity<List<ProfileRequestDTO>> request = RequestEntity
				.post(new URI(profileProperties.getLocationURL() + "/profiles/search/findPartyDetails"))
				.accept(MediaType.APPLICATION_JSON).body(profileRequestDTO);
	ResponseEntity<Map<String,List<ProfileResponseDTO>>> response=profileRestTemplate.exchange(
			request, new ParameterizedTypeReference<Map<String, List<ProfileResponseDTO>>>() {
				});
		return getResponseBody(response);
	}
	
	@HystrixCommand
	public Map<String, List<ProfileResponseDTO>> findPartyCodeDetails(List<ProfileResponseDTO> profileResponseDTO)
			throws URISyntaxException {
		log.debug("profile client method to find List of Profile Details");
		RequestEntity<List<ProfileResponseDTO>> request = RequestEntity
				.post(new URI(profileProperties.getLocationURL() + "/profiles/search/findPartyCodeDetails"))
				.accept(MediaType.APPLICATION_JSON).body(profileResponseDTO);
		ResponseEntity<Map<String, List<ProfileResponseDTO>>> response = profileRestTemplate.exchange(request,
				new ParameterizedTypeReference<Map<String, List<ProfileResponseDTO>>>() {
				});
		return getResponseBody(response);
	}
}