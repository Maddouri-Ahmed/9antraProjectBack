package tn.esprit.PFE.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.PFE.dto.UserDto;
import tn.esprit.PFE.entities.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping( source = "id",target = "id")
    @Mapping(source = "email",target = "email")
    @Mapping( source = "username",target = "username")

    UserDto userToUserDTO(User user);
    List<UserDto> usersToUsersDTO(List<User> users);
}
