const { VITE_BASE_URL_SERVER } = import.meta.env || {};

const { VITE_BASE_URL_CLIENT } = import.meta.env || {};

// API URL
export const API_URL = `${VITE_BASE_URL_SERVER}/api/v1` as string;

// DOMAIN
export const DOMAIN_BACKEND = `${VITE_BASE_URL_SERVER}` as string;

export const DOMAIN_FRONTEND = `${VITE_BASE_URL_CLIENT}` as string;

export const URL_OAUTH2_GOOGLE =
  `${DOMAIN_BACKEND}/oauth2/authorize/google?redirect_uri=` as string;

  export const URL_FRONTEND = `${DOMAIN_FRONTEND}/redirect`;

// COMMON API
export const PREFIX_API_COMMON = `${API_URL}/common` as string;

// AUTH API
export const PREFIX_API_AUTH = `${API_URL}/auth` as string;

// ADMIN API
export const PREFIX_API_SUBJECT_ADMIN = `${API_URL}/admin/subjects` as string;

export const PREFIX_API_ROLE_ADMIN = `${API_URL}/admin/roles` as string;
