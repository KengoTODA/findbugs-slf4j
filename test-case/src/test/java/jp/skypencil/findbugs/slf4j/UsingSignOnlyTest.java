package jp.skypencil.findbugs.slf4j;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Multimap;

public class UsingSignOnlyTest {
  @Test
  public void test() {
    Map<String, Integer> expected = Collections.singletonMap("SLF4J_SIGN_ONLY_FORMAT", 2);
    Multimap<String, String> longMessages = new XmlParser().expect(pkg.UsingSignOnly.class, expected);
    assertThat(longMessages.get("SLF4J_SIGN_ONLY_FORMAT"), hasItem("To make log readable, log format ({}) should contain non-sign character."));
    assertThat(longMessages.get("SLF4J_SIGN_ONLY_FORMAT"), hasItem("To make log readable, log format ({}, {}) should contain non-sign character."));
  }
}
