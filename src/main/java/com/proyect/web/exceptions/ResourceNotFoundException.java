package com.proyect.web.exceptions;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String nameOfResource;
    private String nameOfField;
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
