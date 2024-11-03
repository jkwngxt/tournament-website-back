package ku.th.tournamentwebsiteback.controller;

import ku.th.tournamentwebsiteback.request.StaffRelationshipRequest;
import ku.th.tournamentwebsiteback.response.StaffRelationshipResponse;
import ku.th.tournamentwebsiteback.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/staffs")
public class StaffController {

    @Autowired
    private StaffService staffService;

    // register staff
    @PostMapping
    public ResponseEntity<StaffRelationshipResponse> createStaffRelationship(@RequestBody StaffRelationshipRequest request) {
        StaffRelationshipResponse response = staffService.createStaffRelationship(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<StaffRelationshipResponse>> getAllStaffRelationships() {
        List<StaffRelationshipResponse> responses = staffService.getAllStaffRelationships();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<List<StaffRelationshipResponse>> getStaffRelationshipsByTournament(@PathVariable UUID tournamentId) {
        List<StaffRelationshipResponse> responses = staffService.getStaffsByTournamentId(tournamentId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<StaffRelationshipResponse>> getStaffRelationshipsByUser(@PathVariable Integer userId) {
        List<StaffRelationshipResponse> responses = staffService.getStaffsByUserId(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}/tournament/{tournamentId}")
    public ResponseEntity<StaffRelationshipResponse> getStaffRelationshipByUserAndTournament(
            @PathVariable Integer userId,
            @PathVariable UUID tournamentId) {
        StaffRelationshipResponse response = staffService.getStaffByUserAndTournament(userId, tournamentId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/user/{userId}/tournament/{tournamentId}/validate")
    public ResponseEntity<StaffRelationshipResponse> updateStaffRelationship(
            @PathVariable Integer userId,
            @PathVariable UUID tournamentId,
            @RequestBody StaffRelationshipRequest request) {
        StaffRelationshipResponse response = staffService.validateStaffRelationship(userId, tournamentId, request);
        return ResponseEntity.ok(response);
    }
}
