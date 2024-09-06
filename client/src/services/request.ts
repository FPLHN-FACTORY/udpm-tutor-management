import { ROUTES_CONSTANTS } from "@/constants/path";
import {
  ACCESS_TOKEN_STORAGE_KEY,
  REFRESH_TOKEN_STORAGE_KEY,
  USER_INFO_STORAGE_KEY,
} from "@/constants/storageKey";
import { API_URL, PREFIX_API_AUTH } from "@/constants/url";
import { localStorageAction } from "@/utils/storage";
import axios from "axios";

const request = axios.create({
  baseURL: `${API_URL}/api/`,
});

request.interceptors.request.use((config) => {
  const accessToken = localStorageAction.get(ACCESS_TOKEN_STORAGE_KEY);
  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`;
  }
  return config;
});

request.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const originalRequest = error.config;

    if (
      error.response &&
      error.response.status === 401 &&
      !originalRequest._retry &&
      window.location.pathname !== ROUTES_CONSTANTS.LOGIN.path
    ) {
      originalRequest._retry = true;

      const refreshToken = localStorageAction.get(REFRESH_TOKEN_STORAGE_KEY);
      if (refreshToken) {
        try {
          const response = await axios.post(`${PREFIX_API_AUTH}/refresh`, {
            refresh: refreshToken,
          });
          const newAccessToken = response.data.access_token;
          localStorageAction.set(ACCESS_TOKEN_STORAGE_KEY, newAccessToken);
          originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
          return request(originalRequest);
        } catch (refreshError) {
          console.log("🚀 ~ refreshError:", refreshError)
        }
      }

      localStorageAction.remove(ACCESS_TOKEN_STORAGE_KEY);
      localStorageAction.remove(REFRESH_TOKEN_STORAGE_KEY);
      localStorageAction.remove(USER_INFO_STORAGE_KEY);
      window.location.href = ROUTES_CONSTANTS.LOGIN.path;
    }

    return Promise.reject(error);
  }
);

export default request;
