package codes.cyrus.ticketboard.document;

public enum Role {
	/**
	 * Role descriptions:
	 *  * USER - can create and modify tickets in the projects that they are associated with
	 *  * ADMIN - can create projects and create/modify users within those projects
	 *  * SUPERADMIN - can create projects and create/modify all users and projects
	 */
	USER, ADMIN, SUPERADMIN
}
