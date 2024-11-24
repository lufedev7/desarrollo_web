package com.proyect.web.exceptions;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@Getter
@Schema(description = "Excepción lanzada cuando no se encuentra un recurso")
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Nombre del recurso", example = "Usuario")
    private String nameOfResource;

    @Schema(description = "Nombre del campo", example = "id")
    private String nameOfField;

    @Schema(description = "Valor buscado", example = "123")
    private long valueField;

    public ResourceNotFoundException(String NameOfResource, String NameOfField, long ValueField) {
        super(String.format("%s No Found with : %s : '%s'", NameOfResource, NameOfField, ValueField));
        nameOfResource = NameOfResource;
        nameOfField = NameOfField;
        valueField = ValueField;
    }

    public void setNameOfResource(String nameOfResource) {
        this.nameOfResource = nameOfResource;
    }

    public void setNameOfField(String nameOfField) {
        this.nameOfField = nameOfField;
    }

    public void setValueField(long valueField) {
        this.valueField = valueField;
    }
}
