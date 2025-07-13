package me.zhulin.shopapi.service.impl;

import me.zhulin.shopapi.entity.User;
import me.zhulin.shopapi.exception.MyException;
import me.zhulin.shopapi.repository.CartRepository;
import me.zhulin.shopapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
public class UserServiceImplTest {

  @InjectMocks private UserServiceImpl userService;

  @Mock private UserRepository userRepository;

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private CartRepository cartRepository;

  private User user;

  @BeforeEach
  public void setUp() {
    user = new User();
    user.setPassword("password");
    user.setEmail("email@email.com");
    user.setName("Name");
    user.setPhone("Phone Test");
    user.setAddress("Address Test");
  }

  @Test
  public void createUserTest() {
    when(userRepository.save(user)).thenReturn(user);

    userService.save(user);

    Mockito.verify(userRepository, Mockito.times(2)).save(user);
  }

  @Test
  public void createUserExceptionTest() {
    assertThatThrownBy(() -> userService.save(user)).isInstanceOf(MyException.class);
  }

  @Test
  public void updateTest() {
    User oldUser = new User();
    oldUser.setEmail("email@test.com");

    when(userRepository.findByEmail(user.getEmail())).thenReturn(oldUser);
    when(userRepository.save(oldUser)).thenReturn(oldUser);

    User userResult = userService.update(user);

    assertThat(userResult.getName()).isEqualTo(oldUser.getName());
  }
}
