package com.ead.course.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_USERS")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private UUID userId;
}
