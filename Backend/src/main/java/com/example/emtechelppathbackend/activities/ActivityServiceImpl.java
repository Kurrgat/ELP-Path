package com.example.emtechelppathbackend.activities;

import com.example.emtechelppathbackend.activityattendees.ActivityAttendeesService;
import com.example.emtechelppathbackend.chapter.Chapter;
import com.example.emtechelppathbackend.chapter.ChapterRepo;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.hubs.Hub;
import com.example.emtechelppathbackend.hubs.HubsRepo;
import com.example.emtechelppathbackend.image.Image;
import com.example.emtechelppathbackend.image.ImageService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

	private final ActivityRepository activityRepository;
	private final ChapterRepo chapterRepo;
	private final HubsRepo hubsRepo;
	private final ModelMapper modelMapper;
	private final EntityManager entityManager;
	private final ActivityAttendeesService activityAttendeesService;
	private final ImageService imageService;


	//CRUD
	@Override
	public void addNewChapterActivity(Activity activity, Long chapterId, MultipartFile activityImage) throws NoResourceFoundException, IOException {
		Chapter chapter = chapterRepo.findById(chapterId)
				.orElseThrow(() -> new NoResourceFoundException("Chapter of ID " + chapterId + " does not exist"));

		activity.setChapter(chapter);

		if (activityImage != null && !activityImage.isEmpty()) {
			Image image = imageService.handleImageUpload(activityImage);
			activity.setActivityImage(image);
		}

		activityRepository.save(activity);

		//notifying chapter members of the activity in the background without interrupting the system flow
		  CompletableFuture.runAsync(()-> activityAttendeesService.notifyChapterMembersAboutANewActivity(activity));
	}

	@Override
	public void addNewHubActivity(Activity activity, Long hubId, MultipartFile activityImage) throws NoResourceFoundException, IOException {
		Hub hub = hubsRepo.findById(hubId)
				.orElseThrow(() -> new NoResourceFoundException("Hub of ID " + hubId + " does not exist"));

		activity.setHub(hub);

		if (activityImage != null && !activityImage.isEmpty()) {
			Image image = imageService.handleImageUpload(activityImage);
			activity.setActivityImage(image);
		}

		activityRepository.save(activity);

		//notifying hub members of the activity in the background without interrupting the system flow
		CompletableFuture.runAsync(()-> activityAttendeesService.notifyHubMembersAboutANewActivity(activity));
	}

	@Override
	public List<Activity> displayAllActivities() throws NoResourceFoundException {
		List<Activity> activities = activityRepository.findAll();
		return Optional.ofNullable(activities)
				.filter(list -> !list.isEmpty()) // Check if the list is not empty
				.orElseThrow(() -> new NoResourceFoundException("No records present"));
	}

	@Override
	public Activity displayActivityById(Long activityId) throws NoResourceFoundException {
		return activityRepository.findById(activityId)
				.orElseThrow(() -> new NoResourceFoundException("Activity not found"));
	}

	@Override
	public Activity updateActivityById(Long activityId, ActivityDTO updatedActivityDTO, MultipartFile activityImage) throws NoResourceFoundException, IOException {
		Optional<Activity> existingActivityOption = activityRepository.findById(activityId);
		Activity existingActivity = existingActivityOption.orElseThrow(() -> new NoResourceFoundException("The activity to be updated does not exist"));

		modelMapper.map(updatedActivityDTO, existingActivity);

		if (activityImage != null && !activityImage.isEmpty()) {
			// saving image
			Image image = imageService.handleImageUpload(activityImage);
			existingActivity.setActivityImage(image);
		}

		return activityRepository.save(existingActivity);
	}

	@Override
	public void deleteActivityById(Long activityId) throws NoResourceFoundException {
		Activity activityToDelete = activityRepository.findById(activityId)

				.orElseThrow(() -> new NoResourceFoundException("Activity to be deleted not found"));
		activityRepository.delete(activityToDelete);
	}
	//END OF CRUD


	  @Override
	  public List<Activity> displayActivityByChapterId(Long chapterId) throws NoResourceFoundException {
		    List<Activity> possibleActivities = activityRepository.findActivityByChapterId(chapterId);
		    if (possibleActivities.isEmpty()) {
				throw new NoResourceFoundException("This Chapter has no activities associated to it");
		    } else {
				return possibleActivities;
		    }
	  }

	@Override
	public List<Activity> displayActivityByHubId(Long hubId) throws NoResourceFoundException {
		List<Activity> possibleActivities = activityRepository.findActivityByHubId(hubId);
		if (possibleActivities.isEmpty()) {
			throw new NoResourceFoundException("This hub has no activities associated with it");
		} else {
			return possibleActivities;
		}
	}

	  @Override
	  public long getTotalActivities() {
		    String queryString = "SELECT COUNT(a) FROM Activity a";
		    TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
		    return query.getSingleResult();
	  }

	  @Override
	  public long getTotalActivitiesByChapterId(Long chapterId) {
		    String queryString = "SELECT COUNT(a) FROM Activity a WHERE a.chapter.id = :chapterId";
		    TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
		    query.setParameter("chapterId", chapterId);
		    return query.getSingleResult();
	  }

	@Override
	public long getTotalActivitiesByHubId(Long hubId) {
		String queryString = "SELECT COUNT(a) FROM Activity a WHERE a.hub.id = :hubId";
		TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
		query.setParameter("hubId", hubId);
		return query.getSingleResult();
	}

	  @Override
	  public List<Activity> displayScheduledActivities() {
		    String queryString = "SELECT a FROM Activity a WHERE a.activityStatus =:status";
		    TypedQuery<Activity> query = entityManager.createQuery(queryString, Activity.class);
		    query.setParameter("status", ActivityStatus.SCHEDULED);
		    return query.getResultList();
	  }

	  @Override
	  public long getTotalScheduledActivities() {
		    String queryString = "SELECT COUNT (a) FROM Activity a WHERE a.activityStatus = :status";
		    TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
		    query.setParameter("status", ActivityStatus.SCHEDULED);
		    return query.getSingleResult();
	  }

	  @Override
	  public List<Activity> displayPastActivities() {
		    String queryString = "SELECT a FROM Activity a WHERE a.activityStatus =:status";
		    TypedQuery<Activity> query = entityManager.createQuery(queryString, Activity.class);
		    query.setParameter("status", ActivityStatus.PAST);
		    return query.getResultList();
	  }

	  @Override
	  public long getTotalPastActivities() {
		    String queryString = "SELECT COUNT (a) FROM Activity a WHERE a.activityStatus = :status";
		    TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
		    query.setParameter("status", ActivityStatus.PAST);
		    return query.getSingleResult();
	  }

	  @Override
	  public List<Activity> displayActiveActivities() {
		    String queryString = "SELECT a FROM Activity a WHERE a.activityStatus =:status";
		    TypedQuery<Activity> query = entityManager.createQuery(queryString, Activity.class);
		    query.setParameter("status", ActivityStatus.ONGOING);
		    return query.getResultList();
	  }

	  @Override
	  public long getTotalActiveActivities() {
		    String queryString = "SELECT COUNT (a) FROM Activity a WHERE a.activityStatus = :status";
		    TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
		    query.setParameter("status", ActivityStatus.ONGOING);
		    return query.getSingleResult();
	  }

	  @Override
	  public List<Activity> displayActivitiesByDate(LocalDate activityDate) {

		    //convert LocalDate to localDateTime by setting time to midnight
		    LocalDateTime startDateTime = activityDate.atStartOfDay();

		    //Calculate the end of the day
		    LocalDateTime endDateTime = activityDate.atTime(LocalTime.MAX);

		    try {
				String queryString = "SELECT a FROM Activity a WHERE a.activityDate >= :startDateTime AND a.activityDate <= :endDateTime";
				TypedQuery<Activity> query = entityManager.createQuery(queryString, Activity.class);
				query.setParameter("startDateTime", startDateTime);
				query.setParameter("endDateTime", endDateTime);

				return query.getResultList();
		    } finally {
				entityManager.close();
		    }
	  }

	  @Override
	  public long getTotalActivitiesByDate(LocalDate activityDate) {

		    // Convert LocalDate to LocalDateTime by setting time to midnight
		    LocalDateTime startDateTime = activityDate.atStartOfDay();

		    // Calculate the end of the day
		    LocalDateTime endDateTime = activityDate.atTime(LocalTime.MAX);
		    try {
				String queryString = "SELECT COUNT(a) FROM Activity a WHERE a.activityDate >= :startDateTime AND a.activityDate <= :endDateTime";
				TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
				query.setParameter("startDateTime", startDateTime);
				query.setParameter("endDateTime", endDateTime);

				return query.getSingleResult();
		    }finally {
				entityManager.close();
		    }
	  }


	  @Override
	  public void updateActivityStatus(Activity activity) {
		    LocalDateTime now = LocalDateTime.now();
		    LocalDateTime activityDateTime = activity.getActivityDate();

		    if (activityDateTime.isBefore(now.minusHours(5))) {
				activity.setActivityStatus(ActivityStatus.PAST);
		    } else if (activityDateTime.isAfter(now.plusHours(1))) {
				activity.setActivityStatus(ActivityStatus.SCHEDULED);
		    } else {
				activity.setActivityStatus(ActivityStatus.ONGOING);
		    }
	  }

}