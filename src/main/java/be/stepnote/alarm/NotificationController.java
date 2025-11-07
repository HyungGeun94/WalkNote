package be.stepnote.alarm;

import be.stepnote.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ApiResponse<?> getNotifications(
        @ParameterObject
        @PageableDefault(size = 10,page=0,sort = "id",direction = Sort.Direction.DESC) Pageable pageable)
    {
        return ApiResponse.success(notificationService.getNotifications(pageable));
    }

}