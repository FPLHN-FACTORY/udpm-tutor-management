const { VITE_BASE_URL_SERVER } = import.meta.env || {};

export const API_URL = `${VITE_BASE_URL_SERVER}/api/v1` as string;

export const PREFIX_API_COMMON = `${API_URL}/common` as string;

// ADMIN API
export const PREFIX_API_SUBJECT_ADMIN = `${API_URL}/admin/subjects` as string;
