package com.sosnovich.skyward.mapping;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

/**
 * Configuration class for MapStruct mappers.
 * This configuration specifies the component model and the policies for unmapped properties.
 */
@MapperConfig(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public class MappersConfiguration {
}
