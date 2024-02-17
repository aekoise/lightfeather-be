package io.lightfeather.springtemplate;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
public class ApplicationConltroller {
	private final static String MSG_FORMAT = "%s - %s %s";

	private final ManagerServiceClient managerServiceClient;

	public ApplicationConltroller(ManagerServiceClient managerServiceClient) {
		this.managerServiceClient = managerServiceClient;
	}

	@GetMapping("/api/supervisors")
	public Collection<String> getSupervisors() {
		Comparator<Manager> managerComparators = Comparator.comparing(Manager::jurisdiction)
				.thenComparing(Manager::firstName).thenComparing(Manager::lastName);

		List<String> supervisorsData = managerServiceClient.getManagers().stream()
				.filter(mgr -> !isNumber(mgr.jurisdiction)).sorted(managerComparators)
				.map(mgr -> MSG_FORMAT.formatted(mgr.jurisdiction, mgr.firstName, mgr.lastName))
				.collect(Collectors.toList());

		return supervisorsData;
	}

	@PostMapping("/api/submit")
	@ResponseStatus(value = HttpStatus.OK)
	public void submit(@Valid @RequestBody Account account) {
		System.out.println(account);
	}

	private static boolean isNumber(String value) {
		if (value == null) {
			return false;
		}

		return value.chars().allMatch(Character::isDigit);
	}

	public static record Manager(String id, String phone, String jurisdiction, String identificationNumber,
			String firstName, String lastName) {
	};

	public static class Account {

		@NotBlank
		private String firstName;
		@NotBlank
		private String lastName;
		private String email;
		private String phoneNumber;

		@NotBlank
		private String supervisor;

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		public String getSupervisor() {
			return supervisor;
		}

		public void setSupervisor(String supervisor) {
			this.supervisor = supervisor;
		}

		@Override
		public String toString() {
			return "Account [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", phoneNumber="
					+ phoneNumber + ", supervisor=" + supervisor + "]";
		}

	}

}
