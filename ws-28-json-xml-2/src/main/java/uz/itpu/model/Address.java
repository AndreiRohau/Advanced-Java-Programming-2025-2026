package uz.itpu.model;

/**
 * Immutable employee address parsed from XML.
 */
public final class Address {

	private final String city;
	private final String country;
	private final String zipCode;

	/**
	 * Creates immutable address instance.
	 *
	 * @param city city name
	 * @param country country name
	 * @param zipCode postal code
	 */
	public Address(String city, String country, String zipCode) {
		this.city = city;
		this.country = country;
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public String getZipCode() {
		return zipCode;
	}
}


