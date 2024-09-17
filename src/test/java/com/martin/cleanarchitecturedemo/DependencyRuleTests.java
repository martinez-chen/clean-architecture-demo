package com.martin.cleanarchitecturedemo;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.martin.cleanarchitecturedemo.archunit.HexagonalArchitecture;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

class DependencyRuleTests {

	@Test
	void validateRegistrationContextArchitecture() {
		HexagonalArchitecture.basePackage("com.martin.cleanarchitecturedemo")

				.withDomainLayer("application.domain")

				.withAdaptersLayer("adapter")
				.incoming("in.web")
				.outgoing("out.persistence")
				.and()

				.withApplicationLayer("application")
				.incomingPorts("port.in")
				.outgoingPorts("port.out")
				.and()

				.withConfiguration("configuration")
				.check(new ClassFileImporter()
						.importPackages("com.martin.cleanarchitecturedemo.."));
	}

	@Test
	void domainModelDoesNotDependOnOutside() {
		noClasses()
				.that()
				.resideInAPackage("com.martin.cleanarchitecturedemo.application.domain.model..")
				.should()
				.dependOnClassesThat()
				.resideOutsideOfPackages(
						"com.martin.cleanarchitecturedemo.application.domain.model..",
						"lombok..",
						"java.."
				)
				.check(new ClassFileImporter()
						.importPackages("com.martin.cleanarchitecturedemo.."));
	}

}
