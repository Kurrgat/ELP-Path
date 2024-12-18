package com.example.emtechelppathbackend.activities.activitiesv2;

import com.example.emtechelppathbackend.activityattendees.activityattendeesv2.ActivityAttendeesServicev2;
import com.example.emtechelppathbackend.activitytypes.activitytypesv2.ActivityTypeRepositoryv2;
import com.example.emtechelppathbackend.activitytypes.activitytypesv2.ActivityTypeV2;
import com.example.emtechelppathbackend.chapter.ChapterRepoV2;
import com.example.emtechelppathbackend.chapter.ChapterV2;

import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.hubs.hubsv2.HubsRepov2;
import com.example.emtechelppathbackend.hubs.hubsv2.Hubv2;
import com.example.emtechelppathbackend.jobopportunities.JobOpportunityService;
import com.example.emtechelppathbackend.utils.CustomResponse;
import com.example.emtechelppathbackend.utils.HostNameCapture;
import com.example.emtechelppathbackend.utils.ServerPortService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityServiceImplv2 implements ActivityServicev2 {

	private final ActivityRepositoryv2 activityRepository;
	private final ActivityTypeRepositoryv2 activityTypeRepositoryv2;
	private final ChapterRepoV2 chapterRepo;
	private final HubsRepov2 hubsRepo;
	private final ModelMapper modelMapper;
	private final EntityManager entityManager;
	private final ActivityAttendeesServicev2 activityAttendeesService;
	private final JobOpportunityService jobOpportunityService;
	private final Path uploadPath = Paths.get(System.getProperty("user.dir")+"/images/");
    String imagesPath;

	@Autowired
	ServerPortService serverPortService;


	//CRUD
	@Override
	public CustomResponse<?> addNewChapterActivity(ActivityV2 activity, Long activityTypeId, Long chapterId, MultipartFile activityImage) throws NoResourceFoundException, IOException {
		CustomResponse<ActivityV2> response = new CustomResponse<>();

		try {
			Optional<ChapterV2> optionalChapter = chapterRepo.findById(chapterId);
			Optional<ActivityTypeV2> optionalActivityTypeV2 = activityTypeRepositoryv2.findById(activityTypeId);

			if (optionalChapter.isEmpty()) {
				response.setMessage("Chapter of ID " + chapterId + " does not exist");
				response.setStatusCode(HttpStatus.NOT_FOUND.value());
				response.setSuccess(false);
			} else if (optionalActivityTypeV2.isEmpty()) {
				response.setMessage("ActivityType of ID " + activityTypeId + " does not exist");
				response.setStatusCode(HttpStatus.NOT_FOUND.value());
				response.setSuccess(false);
			} else {
				activity.setChapter(optionalChapter.get());
				activity.setActivityType(optionalActivityTypeV2.get());

				if (activityImage != null && !activityImage.isEmpty()) {
					String fileName = activityImage.getOriginalFilename();
					assert fileName != null;

					if (fileName.endsWith(".jpeg") || fileName.endsWith(".png") | fileName.endsWith(".jpg")) {
						String uniqueName = fileName + fileName.substring(fileName.lastIndexOf('.'));
						activity.setActivityImage(uniqueName);

						activityImage.transferTo(uploadPath.resolve(uniqueName));

						activityRepository.save(activity);

						response.setMessage("Activity added");
						response.setStatusCode(HttpStatus.OK.value());
						response.setPayload(activity);

						// Notifying chapter members of the activity in the background without interrupting the system flow
						CompletableFuture.runAsync(() -> activityAttendeesService.notifyChapterMembersAboutANewActivity(activity));
					} else {
						response.setMessage("Upload images of type .jpg, .jpeg or .png");
						response.setStatusCode(HttpStatus.BAD_REQUEST.value());
						response.setSuccess(false);
					}
				} else {
					activityRepository.save(activity);

					response.setMessage("Activity added");
					response.setStatusCode(HttpStatus.OK.value());
					response.setPayload(activity);

					// Notifying chapter members of the activity in the background without interrupting the system flow
					CompletableFuture.runAsync(() -> activityAttendeesService.notifyChapterMembersAboutANewActivity(activity));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage(e.getMessage());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setSuccess(false);
		}
		return response;
	}


	@Override
	public CustomResponse<?> addNewHubActivity(ActivityV2 activity, Long activityTypeId, Long hubId, MultipartFile activityImage) throws NoResourceFoundException, IOException {
		CustomResponse<ActivityV2> response = new CustomResponse<>();

		try {
			Optional<Hubv2> optionalHub = hubsRepo.findById(hubId);
			Optional<ActivityTypeV2> optionalActivityTypeV2 = activityTypeRepositoryv2.findById(activityTypeId);

			if (optionalHub.isEmpty()) {
				response.setMessage("Hub of ID " + hubId + " does not exist");
				response.setStatusCode(HttpStatus.NOT_FOUND.value());
				response.setSuccess(false);
			} else if (optionalActivityTypeV2.isEmpty()) {
				response.setMessage("ActivityType of ID " + activityTypeId + " does not exist");
				response.setStatusCode(HttpStatus.NOT_FOUND.value());
				response.setSuccess(false);
			} else {
				activity.setHub(optionalHub.get());
				activity.setActivityType(optionalActivityTypeV2.get());

				if (activityImage != null && !activityImage.isEmpty()) {
					String fileName = activityImage.getOriginalFilename();
					assert fileName != null;

					if (fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
						String uniqueName = fileName + fileName.substring(fileName.lastIndexOf('.'));
						activity.setActivityImage(uniqueName);

						activityImage.transferTo(uploadPath.resolve(uniqueName));

						activityRepository.save(activity);

						response.setMessage("Activity added");
						response.setStatusCode(HttpStatus.OK.value());
						response.setPayload(activity);

						// Notifying hub members of the activity in the background without interrupting the system flow
						CompletableFuture.runAsync(() -> activityAttendeesService.notifyHubMembersAboutANewActivity(activity));
					} else {
						response.setMessage("Upload images of type .jpeg, .jpg or .png");
						response.setStatusCode(HttpStatus.BAD_REQUEST.value());
						response.setSuccess(false);
					}
				} else {
					activityRepository.save(activity);

					response.setMessage("Activity added");
					response.setStatusCode(HttpStatus.OK.value());
					response.setPayload(activity);

					// Notifying hub members of the activity in the background without interrupting the system flow
					CompletableFuture.runAsync(() -> activityAttendeesService.notifyHubMembersAboutANewActivity(activity));
				}
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setSuccess(false);
		}
		return response;
	}


	@Override
	public CustomResponse<List<ActivityV2>> displayAllActivities() throws NoResourceFoundException {
		CustomResponse<List<ActivityV2>> response = new CustomResponse<>();

		try {
			List<ActivityV2> activities = activityRepository.findAll();
			
			if(activities.isEmpty()) {
				response.setMessage("No activities found");
				response.setStatusCode(HttpStatus.NOT_FOUND.value());
				response.setSuccess(false);
			} else {
				for(ActivityV2 activity : activities) {
					if(activity.getActivityImage() != null && !activity.getActivityImage().isEmpty()) {
						String activityImage = jobOpportunityService.getImagesPath()+activity.getActivityImage();
						activity.setActivityImage(activityImage);
					}
				}

				response.setMessage("Found "+activities.size()+" activities");
				response.setStatusCode(HttpStatus.OK.value());
				response.setPayload(activities);
			}
		} catch (Exception e) {
				response.setMessage(e.getMessage());
				response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.setSuccess(false);
		}
		return response;

	}

	@Override
	public CustomResponse<ActivityDTOViewv2> displayActivityById(Long activityId) throws NoResourceFoundException {
		CustomResponse<ActivityDTOViewv2> response = new CustomResponse<>();

		try {
			Optional<ActivityV2> optionalActivity = activityRepository.findById(activityId);

			if(optionalActivity.isEmpty()) {
				response.setMessage("No activity found");
				response.setStatusCode(HttpStatus.NOT_FOUND.value());
				response.setSuccess(false);
			} else {
				ActivityV2 activity = optionalActivity.get();

				if (activity.getActivityImage() != null && !activity.getActivityImage().isEmpty()) {
					String activityImage = jobOpportunityService.getImagesPath()+optionalActivity.get().getActivityImage();
					activity.setActivityImage(activityImage);
				}

				ActivityDTOViewv2 responseDTO = modelMapper.map(activity, ActivityDTOViewv2.class);

				response.setMessage("Activity found");
				response.setStatusCode(HttpStatus.OK.value());
				response.setPayload(responseDTO);
			}
		} catch (Exception e) {
				response.setMessage(e.getMessage());
				response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.setSuccess(false);
		}
		return response;
	}

	@Override
	public CustomResponse<ActivityDTOv2> updateActivityById(Long activityId, ActivityDTOv2 updatedActivityDTO, MultipartFile activityImage) throws NoResourceFoundException, IOException {
		CustomResponse<ActivityDTOv2> response = new CustomResponse<>();

		try {
			Optional<ActivityV2> existingActivityOption = activityRepository.findById(activityId);

			if(existingActivityOption.isEmpty()) {
				response.setMessage("The activity to be updated does not exist");
				response.setStatusCode(HttpStatus.NOT_FOUND.value());
				response.setSuccess(false);
			} else {
				ActivityV2 existingActivity = existingActivityOption.get();
				modelMapper.map(updatedActivityDTO, existingActivity);

				if (activityImage != null && !activityImage.isEmpty()) {
					// saving image
					String imageName = activityImage.getOriginalFilename();
					assert imageName != null;

					if(imageName.endsWith(".png") || imageName.endsWith(".jpg") | imageName.endsWith(".jpeg")) {
						String uniqueName = imageName != null ? imageName+imageName.substring(imageName.lastIndexOf('.')) : null;

						activityImage.transferTo(uploadPath.resolve(uniqueName));
						existingActivity.setActivityImage(uniqueName);

						ActivityDTOv2 responseDTO = modelMapper.map(existingActivity, ActivityDTOv2.class);

						activityRepository.save(existingActivity);

						response.setMessage("Activity updated successfully");
						response.setStatusCode(HttpStatus.OK.value());
						response.setPayload(responseDTO);
					} else {
						response.setMessage("Upload images of type .jpeg, .png or .jpg");
						response.setStatusCode(HttpStatus.BAD_REQUEST.value());
						response.setSuccess(false);
					}
				} else {
					ActivityDTOv2 responseDTO = modelMapper.map(existingActivity, ActivityDTOv2.class);

					activityRepository.save(existingActivity);

					response.setMessage("Activity updated successfully");
					response.setStatusCode(HttpStatus.OK.value());
					response.setPayload(responseDTO);
				}
			}

		} catch (Exception e) {
				response.setMessage(e.getMessage());
				response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.setSuccess(false);
		}
		return response;
	}

	@Override
	public CustomResponse<?> deleteActivityById(Long activityId) throws NoResourceFoundException {
		CustomResponse<String> response = new CustomResponse<>();

		try {
			if(!activityRepository.existsById(activityId)) {
				response.setMessage("Activity does not exist");
				response.setStatusCode(HttpStatus.NOT_FOUND.value());
				response.setSuccess(false);
			} else {
				activityRepository.deleteById(activityId);

				response.setMessage(HttpStatus.OK.getReasonPhrase());
				response.setStatusCode(HttpStatus.OK.value());
				response.setPayload("Activity with id "+activityId+"deleted successfully");
			}
		} catch (Exception e) {
				response.setMessage(e.getMessage());
				response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.setSuccess(false);
		}
		return response;
	}
	//END OF CRUD


	  @Override
	  public CustomResponse<List<ActivityV2>> displayActivityByChapterId(Long chapterId) throws NoResourceFoundException {
		CustomResponse<List<ActivityV2>> response = new CustomResponse<>();

		try {
			List<ActivityV2> possibleActivities = activityRepository.findActivityByChapterId(chapterId);

			if (possibleActivities.isEmpty()) {
				response.setMessage("This Chapter has no activities associated to it");
				response.setStatusCode(HttpStatus.NOT_FOUND.value());
				response.setSuccess(false);
		    } else {
				for(ActivityV2 activity: possibleActivities) {
					if(activity.getActivityImage() != null && !activity.getActivityImage().isEmpty()) {
						String activityImage = jobOpportunityService.getImagesPath()+activity.getActivityImage();
						activity.setActivityImage(activityImage);
					}
				}
				response.setMessage("Found "+possibleActivities.size()+" activities");
				response.setStatusCode(HttpStatus.OK.value());
				response.setPayload(possibleActivities);
		    }

		} catch (Exception e) {
				response.setMessage(e.getMessage());
				response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.setSuccess(false);
		}
		return response;
	  }

	@Override
	public CustomResponse<List<ActivityV2>> displayActivityByHubId(Long hubId) throws NoResourceFoundException {
		CustomResponse<List<ActivityV2>> response = new CustomResponse<>();

		try {
			List<ActivityV2> possibleActivities = activityRepository.findActivityByChapterId(hubId);

			if (possibleActivities.isEmpty()) {
				response.setMessage("This Hub has no activities associated to it");
				response.setStatusCode(HttpStatus.NOT_FOUND.value());
				response.setSuccess(false);
		    } else {
				for(ActivityV2 activity: possibleActivities) {
					if(activity.getActivityImage() != null && !activity.getActivityImage().isEmpty()) {
						String activityImage = jobOpportunityService.getImagesPath()+activity.getActivityImage();
						activity.setActivityImage(activityImage);
					}
				}
				response.setMessage("Found "+possibleActivities.size()+" activities");
				response.setStatusCode(HttpStatus.OK.value());
				response.setPayload(possibleActivities);
		    }

		} catch (Exception e) {
				response.setMessage(e.getMessage());
				response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.setSuccess(false);
		}
		return response;
	}

	  @Override
	  public CustomResponse<?> getTotalActivities() {
		CustomResponse<Long> response = new CustomResponse<>();

		try {
			String queryString = "SELECT COUNT(a) FROM Activityv2 a";
		    TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
		    Long activities = query.getSingleResult();

			response.setMessage("Found "+activities+" activities");
			response.setStatusCode(HttpStatus.OK.value());
			response.setPayload(activities);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setSuccess(false);
		}
		return response;
	  }

	  @Override
	  public CustomResponse<?> getTotalActivitiesByChapterId(Long chapterId) {
		CustomResponse<Long> response = new CustomResponse<>();

		try {
			String queryString = "SELECT COUNT(a) FROM Activityv2 a WHERE a.chapter.id = :chapterId";
		    TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
		    query.setParameter("chapterId", chapterId);
		    var activities =  query.getSingleResult();

			response.setMessage("Found "+activities+" activities");
			response.setStatusCode(HttpStatus.OK.value());
			response.setPayload(activities);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setSuccess(false);
		}
		return response;
	  }

	@Override
	public CustomResponse<?> getTotalActivitiesByHubId(Long hubId) {
		CustomResponse<Long> response = new CustomResponse<>();

		try {
			String queryString = "SELECT COUNT(a) FROM Activityv2 a WHERE a.hub.id = :hubId";
			TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
			query.setParameter("hubId", hubId);
			Long activities = query.getSingleResult();

			response.setMessage("Found "+activities+" activities");
			response.setStatusCode(HttpStatus.OK.value());
			response.setPayload(activities);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setSuccess(false);
		}
		return response;
	}

	  @Override
	  public CustomResponse<List<ActivityV2>> displayScheduledActivities() {
		CustomResponse<List<ActivityV2>> response = new CustomResponse<>();
		try {
			String queryString = "SELECT a FROM Activityv2 a WHERE a.activityStatus =:status";
		    TypedQuery<ActivityV2> query = entityManager.createQuery(queryString, ActivityV2.class);
		    query.setParameter("status", ActivityStatusv2.SCHEDULED);
		    var results = query.getResultList();

			if(results.isEmpty()) {
				response.setMessage("No records found");
				response.setStatusCode(HttpStatus.NOT_FOUND.value());
				response.setSuccess(false);
				response.setPayload(results);
			} else {
				for(ActivityV2 activity: results) {
					if(activity.getActivityImage() != null && !activity.getActivityImage().isEmpty()) {
						String activityImage = getImagesPath()+activity.getActivityImage();
						activity.setActivityImage(activityImage);
					}
				}
				response.setMessage("Found"+results+" results.");
				response.setStatusCode(HttpStatus.OK.value());
				response.setPayload(results);
			}

		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setSuccess(false);
		}
		return response;
	  }

	  @Override
	  public CustomResponse<?> getTotalScheduledActivities() {
		CustomResponse<Long> response = new CustomResponse<>();

		try {
		    String queryString = "SELECT COUNT (a) FROM Activityv2 a WHERE a.activityStatus = :status";
		    TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
		    query.setParameter("status", ActivityStatusv2.SCHEDULED);
		    var results = query.getSingleResult();

			response.setMessage("Found "+results+" scheduled activities");
			response.setStatusCode(HttpStatus.OK.value());
			response.setPayload(results);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setSuccess(false);
		}
		return response;
	  }

	  @Override
	  public CustomResponse<List<ActivityV2>> displayPastActivities() {
		CustomResponse<List<ActivityV2>> response = new CustomResponse<>();

		try {
			String queryString = "SELECT a FROM Activityv2 a WHERE a.activityStatus =:status";
		    TypedQuery<ActivityV2> query = entityManager.createQuery(queryString, ActivityV2.class);
		    query.setParameter("status", ActivityStatusv2.PAST);
		    var results = query.getResultList();

			for(ActivityV2 activity: results) {
				if(activity.getActivityImage() != null && !activity.getActivityImage().isEmpty()) {
					String activityImage = getImagesPath()+activity.getActivityImage();
					activity.setActivityImage(activityImage);
				}
			}

			response.setMessage("Found "+results+" past activities");
			response.setStatusCode(HttpStatus.OK.value());
			response.setPayload(results);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setSuccess(false);
		}
		return response;
	  }

	  @Override
	  public CustomResponse<?> getTotalPastActivities() {
		CustomResponse<Long> response = new CustomResponse<>();

		try {
			String queryString = "SELECT COUNT (a) FROM Activityv2 a WHERE a.activityStatus = :status";
		    TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
		    query.setParameter("status", ActivityStatusv2.PAST);
		    var result = query.getSingleResult();

			response.setMessage("Found "+result+" total activities");
			response.setStatusCode(HttpStatus.OK.value());
			response.setPayload(result);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setSuccess(false);
		}
		return response;
	  }

	  @Override
	  public CustomResponse<List<ActivityV2>> displayActiveActivities() {
		CustomResponse<List<ActivityV2>> response = new CustomResponse<>();

		try {
			String queryString = "SELECT a FROM Activityv2 a WHERE a.activityStatus =:status";
		    TypedQuery<ActivityV2> query = entityManager.createQuery(queryString, ActivityV2.class);
		    query.setParameter("status", ActivityStatusv2.ONGOING);
		    var results = query.getResultList();

			for(ActivityV2 activity: results) {
				if(activity.getActivityImage() != null && !activity.getActivityImage().isEmpty()) {
					String activityImage = jobOpportunityService.getImagesPath()+activity.getActivityImage();
					activity.setActivityImage(activityImage);
				}
			}
			response.setMessage("Found "+results+" active activities");
			response.setStatusCode(HttpStatus.OK.value());
			response.setPayload(results);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setSuccess(false);
		}
		return response;
	  }

	  @Override
	  public CustomResponse<?> getTotalActiveActivities() {
		CustomResponse<Long> response = new CustomResponse<>();

		try {
			String queryString = "SELECT COUNT (a) FROM Activityv2 a WHERE a.activityStatus = :status";
		    TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
		    query.setParameter("status", ActivityStatusv2.ONGOING);
		    var result = query.getSingleResult();

			response.setMessage("Found "+result+" activities");
			response.setStatusCode(HttpStatus.OK.value());
			response.setPayload(result);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setSuccess(false);
		}
		return response;
	  }

	  @Override
	  public CustomResponse<List<ActivityV2>> displayActivitiesByDate(LocalDate activityDate) {
		CustomResponse<List<ActivityV2>> response = new CustomResponse<>();

		    //convert LocalDate to localDateTime by setting time to midnight
		    LocalDateTime startDateTime = activityDate.atStartOfDay();

		    //Calculate the end of the day
		    LocalDateTime endDateTime = activityDate.atTime(LocalTime.MAX);

		    try {
				String queryString = "SELECT a FROM Activityv2 a WHERE a.activityDate >= :startDateTime AND a.activityDate <= :endDateTime";
				TypedQuery<ActivityV2> query = entityManager.createQuery(queryString, ActivityV2.class);
				query.setParameter("startDateTime", startDateTime);
				query.setParameter("endDateTime", endDateTime);

				var results = query.getResultList();

				for(ActivityV2 activity : results) {
					if(activity.getActivityImage() != null && !activity.getActivityImage().isEmpty()) {
						String activityImage = jobOpportunityService.getImagesPath()+activity.getActivityImage();
						activity.setActivityImage(activityImage);
					}
				}

				response.setMessage("Found "+results+" activities for "+activityDate);
				response.setStatusCode(HttpStatus.OK.value());
				response.setPayload(results);
		    } catch (Exception e) {
				response.setMessage(e.getMessage());
				response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.setSuccess(false);
			} finally {
				entityManager.close();
		    }
			return response;
	  }

	  @Override
	  public CustomResponse<?> getTotalActivitiesByDate(LocalDate activityDate) {
		CustomResponse<Long> response = new CustomResponse<>();

		    // Convert LocalDate to LocalDateTime by setting time to midnight
		    LocalDateTime startDateTime = activityDate.atStartOfDay();

		    // Calculate the end of the day
		    LocalDateTime endDateTime = activityDate.atTime(LocalTime.MAX);
		    try {
				String queryString = "SELECT COUNT(a) FROM Activityv2 a WHERE a.activityDate >= :startDateTime AND a.activityDate <= :endDateTime";
				TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
				query.setParameter("startDateTime", startDateTime);
				query.setParameter("endDateTime", endDateTime);

				var results = query.getSingleResult();

				response.setMessage("Found "+results+" activities for "+activityDate);
				response.setStatusCode(HttpStatus.OK.value());
				response.setPayload(results);
		    } catch (Exception e) {
				response.setMessage(e.getMessage());
				response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.setSuccess(false);
		    }finally {
				entityManager.close();
		    }
			return response;
	  }


	  @Override
	  public CustomResponse<?> updateActivityStatus(ActivityV2 activity) {
		CustomResponse<String> response = new CustomResponse<>();

		    LocalDateTime now = LocalDateTime.now();
		    LocalDateTime activityDateTime = activity.getActivityDate();

			try {
				if (activityDateTime.isBefore(now.minusHours(5))) {
					activity.setActivityStatus(ActivityStatusv2.PAST);
				} else if (activityDateTime.isAfter(now.plusHours(1))) {
					activity.setActivityStatus(ActivityStatusv2.SCHEDULED);
				} else {
					activity.setActivityStatus(ActivityStatusv2.ONGOING);
				}

				response.setMessage("Activity status updated");
				response.setStatusCode(HttpStatus.OK.value());
				response.setPayload("Status: "+activity.getActivityStatus());
			} catch (Exception e) {
				response.setMessage(e.getMessage());
				response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.setSuccess(false);
			}
			return response;
	  }


	  public String getImagesPath() {
        try {
            HostNameCapture hostNameCapture = new HostNameCapture();
            try {
                int port = serverPortService.getPort();

                if(port > 1023) {
                    imagesPath = hostNameCapture.getHost()+":"+port+"/images/";
                } else {
                    log.debug("Port is reservered for system use");
                }
                // imagesPath = hostNameCapture.getHost()+":5555/images/";
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return imagesPath;
    }

}