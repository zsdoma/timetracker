package hu.zsdoma.timetracker.api.dto;

import java.util.Objects;

/**
 * DTO class for store issue entity.
 */
public class IssueDTO {
    /**
     * Unique issue ID.
     */
    private long id;

    /**
     * Title of issue.
     */
    private String title;

    /**
     * Description of issue.
     */
    private String description;

    /**
     * Constructor with ussue params.
     * 
     * @param id
     *            Id of the issue.
     * @param title
     *            Title of issue.
     * @param description
     *            Descpription of issue.
     */
    public IssueDTO(final long id, final String title, final String description) {
        super();
        nullCheck(title, description);

        this.id = id;
        this.title = title;
        this.description = description;
    }

    /**
     * Getter method for {@code Description}.
     * 
     * @return description value.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter method for {@code id}.
     * 
     * @return id value.
     */
    public long getId() {
        return id;
    }

    /**
     * Getter method for {@code title}.
     * 
     * @return Title value.
     */
    public String getTitle() {
        return title;
    }

    /**
     * String params null check for constructor. Throw {@link NullPointerException} is any parameter is null.
     * @param title
     *            Title of issue.
     * @param description
     *            Description of issue.
     */
    private void nullCheck(final String title, final String description) {
        Objects.requireNonNull(title, "The title must be not null!");
        Objects.requireNonNull(description, "The description must be not null!");
    }

}
