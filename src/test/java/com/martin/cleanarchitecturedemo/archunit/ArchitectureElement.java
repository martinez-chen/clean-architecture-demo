package com.martin.cleanarchitecturedemo.archunit;

import static com.tngtech.archunit.base.DescribedPredicate.greaterThanOrEqualTo;
import static com.tngtech.archunit.lang.conditions.ArchConditions.containNumberOfElements;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import java.util.List;

/**
 * 主要提供幾個用來檢查架構規則的靜態方法與工具方法。
 * <p>
 * 目的是對專案中不同 package 間的依賴性、空 package 等做出規範。
 */
abstract class ArchitectureElement {

  /**
   * 專案的基礎 package。
   * <p>
   * 用來組合完整的 package 名稱。
   */
  final String basePackage;

  public ArchitectureElement(String basePackage) {
    this.basePackage = basePackage;
  }

  /**
   * 組合完整的 package 名稱。
   *
   * @param relativePackage 相對於 basePackage 的 package 名稱
   * @return 完整的 package 名稱
   */
  String fullQualifiedPackage(String relativePackage) {
    return this.basePackage + "." + relativePackage;
  }

  /**
   * 拒絕來源 package 依賴目標 package 的依賴關係。
   *
   * @param fromPackageName 來源 package 名稱
   * @param toPackageName   目標 package 名稱
   * @param classes         要檢查的 JavaClasses
   */
  static void denyDependency(String fromPackageName, String toPackageName, JavaClasses classes) {
    noClasses()
        .that()
        .resideInAPackage(matchAllClassesInPackage(fromPackageName))
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage(matchAllClassesInPackage(toPackageName))
        .check(classes);
  }

  /**
   * 拒絕來源 package 依賴目標 package 的依賴關係。
   *
   * @param fromPackages 來源 package 名稱列表
   * @param toPackages   目標 package 名稱列表
   * @param classes      要檢查的 JavaClasses
   */
  static void denyAnyDependency(
      List<String> fromPackages, List<String> toPackages, JavaClasses classes) {
    for (String fromPackage : fromPackages) {
      for (String toPackage : toPackages) {
        denyDependency(fromPackage, toPackage, classes);
      }
    }
  }

  /**
   * 符合 package 名稱的所有 class。
   *
   * @param packageName package 名稱
   * @return 符合 package 名稱的所有 class
   */
  static String matchAllClassesInPackage(String packageName) {
    return packageName + "..";
  }

  /**
   * 拒絕空 package。
   *
   * @param packageName package 名稱
   */
  void denyEmptyPackage(String packageName) {
    classes()
        .that()
        .resideInAPackage(matchAllClassesInPackage(packageName))
        .should(containNumberOfElements(greaterThanOrEqualTo(1)))
        .check(classesInPackage(packageName));
  }

  private JavaClasses classesInPackage(String packageName) {
    return new ClassFileImporter().importPackages(packageName);
  }

  void denyEmptyPackages(List<String> packages) {
    for (String packageName : packages) {
      denyEmptyPackage(packageName);
    }
  }
}
