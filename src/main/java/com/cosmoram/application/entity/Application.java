package com.cosmoram.application.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    public static final int CODE_LENGTH = 9;
    public static final int NAME_MIN_LENGTH = 3;
    public static final int NAME_MAX_LENGTH = 50;
    public static final int DESC_MIN_LENGTH = 3;
    public static final int DESC_MAX_LENGTH = 250;

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @NotBlank()
    @Size(min = CODE_LENGTH, max = CODE_LENGTH)
    private String code;

    @NotBlank()
    @Size(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH)
    private String name;

    @NotBlank()
    @Size(min = DESC_MIN_LENGTH, max = DESC_MAX_LENGTH)
    private String desc;
}
