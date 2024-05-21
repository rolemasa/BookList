package spring.bookList.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserUpdateRequest extends UserCreate implements Serializable {
	@NotNull
	private int id;

}
