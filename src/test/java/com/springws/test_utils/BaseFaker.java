package com.springws.test_utils;

import net.datafaker.Faker;
import net.datafaker.providers.base.AbstractProvider;
import net.datafaker.providers.base.BaseProviders;

public class BaseFaker {

    public static class AppFaker extends Faker {

        public Email mail() {
            return getProvider(Email.class, Email::new, this);
        }

    }

    public static class Email extends AbstractProvider<BaseProviders> {
        protected Email(BaseProviders faker) {
            super(faker);
        }

        public String random(String name) {
            return name + "@" + faker.domain().fullDomain(faker.company().name());
        }
    }

}
