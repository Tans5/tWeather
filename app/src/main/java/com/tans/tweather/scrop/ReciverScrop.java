package com.tans.tweather.scrop;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by tans on 2018/4/7.
 */
@Scope
@Documented
@Retention(RUNTIME)
public @interface ReciverScrop {
}
