import { API_URL } from "@/constants/url";
import axios from "axios";

const request = axios.create({
  baseURL: `${API_URL}/api/`,
});

export default request;
