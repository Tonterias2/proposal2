package com.spingular.proposal.web.rest;

import com.spingular.proposal.SpingularproposalApp;
import com.spingular.proposal.domain.Profile;
import com.spingular.proposal.domain.User;
import com.spingular.proposal.domain.Proposal;
import com.spingular.proposal.domain.VoteProposal;
import com.spingular.proposal.repository.ProfileRepository;
import com.spingular.proposal.service.ProfileService;
import com.spingular.proposal.service.dto.ProfileDTO;
import com.spingular.proposal.service.mapper.ProfileMapper;
import com.spingular.proposal.web.rest.errors.ExceptionTranslator;
import com.spingular.proposal.service.dto.ProfileCriteria;
import com.spingular.proposal.service.ProfileQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.spingular.proposal.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link ProfileResource} REST controller.
 */
@SpringBootTest(classes = SpingularproposalApp.class)
public class ProfileResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_ASSIGNED_VOTES_POINTS = 1L;
    private static final Long UPDATED_ASSIGNED_VOTES_POINTS = 2L;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileMapper profileMapper;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileQueryService profileQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restProfileMockMvc;

    private Profile profile;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProfileResource profileResource = new ProfileResource(profileService, profileQueryService);
        this.restProfileMockMvc = MockMvcBuilders.standaloneSetup(profileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profile createEntity(EntityManager em) {
        Profile profile = new Profile()
            .creationDate(DEFAULT_CREATION_DATE)
            .assignedVotesPoints(DEFAULT_ASSIGNED_VOTES_POINTS);
        return profile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profile createUpdatedEntity(EntityManager em) {
        Profile profile = new Profile()
            .creationDate(UPDATED_CREATION_DATE)
            .assignedVotesPoints(UPDATED_ASSIGNED_VOTES_POINTS);
        return profile;
    }

    @BeforeEach
    public void initTest() {
        profile = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfile() throws Exception {
        int databaseSizeBeforeCreate = profileRepository.findAll().size();

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);
        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isCreated());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeCreate + 1);
        Profile testProfile = profileList.get(profileList.size() - 1);
        assertThat(testProfile.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testProfile.getAssignedVotesPoints()).isEqualTo(DEFAULT_ASSIGNED_VOTES_POINTS);
    }

    @Test
    @Transactional
    public void createProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profileRepository.findAll().size();

        // Create the Profile with an existing ID
        profile.setId(1L);
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = profileRepository.findAll().size();
        // set the field null
        profile.setCreationDate(null);

        // Create the Profile, which fails.
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProfiles() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].assignedVotesPoints").value(hasItem(DEFAULT_ASSIGNED_VOTES_POINTS.intValue())));
    }
    
    @Test
    @Transactional
    public void getProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get the profile
        restProfileMockMvc.perform(get("/api/profiles/{id}", profile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(profile.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.assignedVotesPoints").value(DEFAULT_ASSIGNED_VOTES_POINTS.intValue()));
    }

    @Test
    @Transactional
    public void getAllProfilesByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where creationDate equals to DEFAULT_CREATION_DATE
        defaultProfileShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the profileList where creationDate equals to UPDATED_CREATION_DATE
        defaultProfileShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProfilesByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultProfileShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the profileList where creationDate equals to UPDATED_CREATION_DATE
        defaultProfileShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProfilesByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where creationDate is not null
        defaultProfileShouldBeFound("creationDate.specified=true");

        // Get all the profileList where creationDate is null
        defaultProfileShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByAssignedVotesPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where assignedVotesPoints equals to DEFAULT_ASSIGNED_VOTES_POINTS
        defaultProfileShouldBeFound("assignedVotesPoints.equals=" + DEFAULT_ASSIGNED_VOTES_POINTS);

        // Get all the profileList where assignedVotesPoints equals to UPDATED_ASSIGNED_VOTES_POINTS
        defaultProfileShouldNotBeFound("assignedVotesPoints.equals=" + UPDATED_ASSIGNED_VOTES_POINTS);
    }

    @Test
    @Transactional
    public void getAllProfilesByAssignedVotesPointsIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where assignedVotesPoints in DEFAULT_ASSIGNED_VOTES_POINTS or UPDATED_ASSIGNED_VOTES_POINTS
        defaultProfileShouldBeFound("assignedVotesPoints.in=" + DEFAULT_ASSIGNED_VOTES_POINTS + "," + UPDATED_ASSIGNED_VOTES_POINTS);

        // Get all the profileList where assignedVotesPoints equals to UPDATED_ASSIGNED_VOTES_POINTS
        defaultProfileShouldNotBeFound("assignedVotesPoints.in=" + UPDATED_ASSIGNED_VOTES_POINTS);
    }

    @Test
    @Transactional
    public void getAllProfilesByAssignedVotesPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where assignedVotesPoints is not null
        defaultProfileShouldBeFound("assignedVotesPoints.specified=true");

        // Get all the profileList where assignedVotesPoints is null
        defaultProfileShouldNotBeFound("assignedVotesPoints.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByAssignedVotesPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where assignedVotesPoints greater than or equals to DEFAULT_ASSIGNED_VOTES_POINTS
        defaultProfileShouldBeFound("assignedVotesPoints.greaterOrEqualThan=" + DEFAULT_ASSIGNED_VOTES_POINTS);

        // Get all the profileList where assignedVotesPoints greater than or equals to UPDATED_ASSIGNED_VOTES_POINTS
        defaultProfileShouldNotBeFound("assignedVotesPoints.greaterOrEqualThan=" + UPDATED_ASSIGNED_VOTES_POINTS);
    }

    @Test
    @Transactional
    public void getAllProfilesByAssignedVotesPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where assignedVotesPoints less than or equals to DEFAULT_ASSIGNED_VOTES_POINTS
        defaultProfileShouldNotBeFound("assignedVotesPoints.lessThan=" + DEFAULT_ASSIGNED_VOTES_POINTS);

        // Get all the profileList where assignedVotesPoints less than or equals to UPDATED_ASSIGNED_VOTES_POINTS
        defaultProfileShouldBeFound("assignedVotesPoints.lessThan=" + UPDATED_ASSIGNED_VOTES_POINTS);
    }


    @Test
    @Transactional
    public void getAllProfilesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        profile.setUser(user);
        profileRepository.saveAndFlush(profile);
        Long userId = user.getId();

        // Get all the profileList where user equals to userId
        defaultProfileShouldBeFound("userId.equals=" + userId);

        // Get all the profileList where user equals to userId + 1
        defaultProfileShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByProposalIsEqualToSomething() throws Exception {
        // Initialize the database
        Proposal proposal = ProposalResourceIT.createEntity(em);
        em.persist(proposal);
        em.flush();
        profile.addProposal(proposal);
        profileRepository.saveAndFlush(profile);
        Long proposalId = proposal.getId();

        // Get all the profileList where proposal equals to proposalId
        defaultProfileShouldBeFound("proposalId.equals=" + proposalId);

        // Get all the profileList where proposal equals to proposalId + 1
        defaultProfileShouldNotBeFound("proposalId.equals=" + (proposalId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByVoteProposalIsEqualToSomething() throws Exception {
        // Initialize the database
        VoteProposal voteProposal = VoteProposalResourceIT.createEntity(em);
        em.persist(voteProposal);
        em.flush();
        profile.addVoteProposal(voteProposal);
        profileRepository.saveAndFlush(profile);
        Long voteProposalId = voteProposal.getId();

        // Get all the profileList where voteProposal equals to voteProposalId
        defaultProfileShouldBeFound("voteProposalId.equals=" + voteProposalId);

        // Get all the profileList where voteProposal equals to voteProposalId + 1
        defaultProfileShouldNotBeFound("voteProposalId.equals=" + (voteProposalId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProfileShouldBeFound(String filter) throws Exception {
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].assignedVotesPoints").value(hasItem(DEFAULT_ASSIGNED_VOTES_POINTS.intValue())));

        // Check, that the count call also returns 1
        restProfileMockMvc.perform(get("/api/profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProfileShouldNotBeFound(String filter) throws Exception {
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProfileMockMvc.perform(get("/api/profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProfile() throws Exception {
        // Get the profile
        restProfileMockMvc.perform(get("/api/profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        int databaseSizeBeforeUpdate = profileRepository.findAll().size();

        // Update the profile
        Profile updatedProfile = profileRepository.findById(profile.getId()).get();
        // Disconnect from session so that the updates on updatedProfile are not directly saved in db
        em.detach(updatedProfile);
        updatedProfile
            .creationDate(UPDATED_CREATION_DATE)
            .assignedVotesPoints(UPDATED_ASSIGNED_VOTES_POINTS);
        ProfileDTO profileDTO = profileMapper.toDto(updatedProfile);

        restProfileMockMvc.perform(put("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isOk());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
        Profile testProfile = profileList.get(profileList.size() - 1);
        assertThat(testProfile.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testProfile.getAssignedVotesPoints()).isEqualTo(UPDATED_ASSIGNED_VOTES_POINTS);
    }

    @Test
    @Transactional
    public void updateNonExistingProfile() throws Exception {
        int databaseSizeBeforeUpdate = profileRepository.findAll().size();

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfileMockMvc.perform(put("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        int databaseSizeBeforeDelete = profileRepository.findAll().size();

        // Delete the profile
        restProfileMockMvc.perform(delete("/api/profiles/{id}", profile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Profile.class);
        Profile profile1 = new Profile();
        profile1.setId(1L);
        Profile profile2 = new Profile();
        profile2.setId(profile1.getId());
        assertThat(profile1).isEqualTo(profile2);
        profile2.setId(2L);
        assertThat(profile1).isNotEqualTo(profile2);
        profile1.setId(null);
        assertThat(profile1).isNotEqualTo(profile2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfileDTO.class);
        ProfileDTO profileDTO1 = new ProfileDTO();
        profileDTO1.setId(1L);
        ProfileDTO profileDTO2 = new ProfileDTO();
        assertThat(profileDTO1).isNotEqualTo(profileDTO2);
        profileDTO2.setId(profileDTO1.getId());
        assertThat(profileDTO1).isEqualTo(profileDTO2);
        profileDTO2.setId(2L);
        assertThat(profileDTO1).isNotEqualTo(profileDTO2);
        profileDTO1.setId(null);
        assertThat(profileDTO1).isNotEqualTo(profileDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(profileMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(profileMapper.fromId(null)).isNull();
    }
}
