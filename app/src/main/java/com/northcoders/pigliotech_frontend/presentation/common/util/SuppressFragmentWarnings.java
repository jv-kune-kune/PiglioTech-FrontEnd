package com.northcoders.pigliotech_frontend.presentation.common.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to suppress common Android Fragment warnings.
 * This is applied to all Fragment classes in the project.
 */
@SuppressWarnings({ "unused", "RedundantSuppression" })
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR })
public @interface SuppressFragmentWarnings {
}