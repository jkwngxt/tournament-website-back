package ku.th.tournamentwebsiteback.service;

import jakarta.persistence.EntityNotFoundException;
import ku.th.tournamentwebsiteback.entity.JoinAsStaffRelationship;
import ku.th.tournamentwebsiteback.repository.JoinAsStaffRepository;
import ku.th.tournamentwebsiteback.request.StaffRelationshipRequest;
import ku.th.tournamentwebsiteback.response.StaffRelationshipResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StaffService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    JoinAsStaffRepository staffRepository;
    @Autowired
    DTOConvertor convertor;

    public List<StaffRelationshipResponse> getAllStaffRelationships() {
        List<JoinAsStaffRelationship> relationships = staffRepository.findAll();
        return relationships.stream()
                .map(this::convertToStaffResponse)
                .collect(Collectors.toList());
    }

    public List<StaffRelationshipResponse> getStaffsByTournamentId(UUID tournamentId) {
        List<JoinAsStaffRelationship> staffRelationships = staffRepository.findByTournamentTournamentId(tournamentId);
        return staffRelationships.stream()
                .map(this::convertToStaffResponse)
                .collect(Collectors.toList());
    }

    public List<StaffRelationshipResponse> getStaffsByUserId(Integer userId) {
        List<JoinAsStaffRelationship> staffRelationships = staffRepository.findByUserUserId(userId);
        return staffRelationships.stream()
                .map(this::convertToStaffResponse)
                .collect(Collectors.toList());
    }

    public StaffRelationshipResponse getStaffByUserAndTournament(Integer userId, UUID tournamentId) {
        JoinAsStaffRelationship staffRelationships = staffRepository.findByUserUserIdAndTournamentTournamentId(userId, tournamentId);
        if (staffRelationships == null) {
            throw new EntityNotFoundException("Staff relationship not found for user " + userId + " in tournament " + tournamentId);
        }

        return convertToStaffResponse(staffRelationships);
    }

    public StaffRelationshipResponse validateStaffRelationship(Integer userId, UUID tournamentId, StaffRelationshipRequest request) {
        JoinAsStaffRelationship staffRelationships = staffRepository.findByUserUserIdAndTournamentTournamentId(userId, tournamentId);
        if (staffRelationships == null) {
            throw new EntityNotFoundException("Staff relationship not found for user " + userId + " in tournament " + tournamentId);
        }
        modelMapper.map(request, staffRelationships);
        return convertToStaffResponse(staffRelationships);
    }

    public StaffRelationshipResponse createStaffRelationship(StaffRelationshipRequest request) {
        JoinAsStaffRelationship record = modelMapper.map(request, JoinAsStaffRelationship.class);
        record.setStatus("Unapproved");
        return convertToStaffResponse(staffRepository.save(record));
    }

    public StaffRelationshipResponse convertToStaffResponse(JoinAsStaffRelationship staff) {
        return convertor.convertToStaffResponse(staff);
    }
}
