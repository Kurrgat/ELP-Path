package com.example.emtechelppathbackend.hubs.hubsv2;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.hubmembers.hubmembersv2.HubMemberv2;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.example.emtechelppathbackend.utils.CustomResponse;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface HubServicev2 {
    CustomResponse<?> createHub(Hubv2 hub, MultipartFile file) throws IOException;
    CustomResponse<?> getHubByUserId( Long userId);
    CustomResponse<List<Hubv2>> getAllHubs();
    CustomResponse<?> joinHub(Long userId, Long hubId);
    CustomResponse<Hubv2> updateHubById(Long hubId, HubDTOUpdatev2 updatedHubDto, MultipartFile hubImage) throws NoResourceFoundException, IOException;
    CustomResponse<?> leaveHub(Long userId, Long hubId);
    CustomResponse<?> deleteHub(Long hubId);
    CustomResponse<?> getHubMembersByHubId(Long hubId);

    CustomResponse<?> setHubAdmin(Long hubId, Long userId, boolean b);

    CustomResponse<?> approveJoiningHub(Long membershipId, boolean approve);


    CustomResponse<?> getMembershipForApproval();

    CustomResponse<?> getHub(Long hubId);
}
