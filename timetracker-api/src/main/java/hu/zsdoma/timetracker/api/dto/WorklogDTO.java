package hu.zsdoma.timetracker.api.dto;

/**
 * DTO class for worklog entry.
 */
public class WorklogDTO {

	/**
	 * Worklog ID.
	 */
	private long id;

	/**
	 * Worklog begin timestamp.
	 */
	private long beginTimestamp;

	/**
	 * Worklog end timestamp.
	 */
	private long endTimeStamp;

	/**
	 * The id of issue that belong this worklog.
	 */
	private long issueId;

	/**
	 * Worklog message.
	 */
	private String message;

	/**
	 * Getter method for worklog start timestamp.
	 * 
	 * @return worklog begin timestamp.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Getter method for worklog begin timestamp.
	 * 
	 * @return worklog begin timestamp.
	 */
	public long getBeginTimestamp() {
		return beginTimestamp;
	}

	/**
	 * Getter method for worklog end timestamp.
	 * 
	 * @return worklog end timestamp.
	 */
	public long getEndTimeStamp() {
		return endTimeStamp;
	}

	/**
	 * Getter method for worklog issue id.
	 * 
	 * @return worklog issue id.
	 */
	public long getIssueId() {
		return issueId;
	}

	/**
	 * Getter method for worklog message.
	 * 
	 * @return worklog message.
	 */
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "WorklogDTO [id=" + id + ", startTimestamp=" + beginTimestamp
				+ ", endTimeStamp=" + endTimeStamp + ", issueId=" + issueId
				+ ", message=" + message + "]";
	}

}
