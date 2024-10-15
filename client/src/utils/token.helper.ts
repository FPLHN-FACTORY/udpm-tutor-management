import { DecodedToken, UserInformation } from "@/types/auth.type";
import dayjs, { Dayjs } from "dayjs";
import { jwtDecode } from "jwt-decode";

export const getUserInformation = (token: string): UserInformation => {
  const decoded = jwtDecode<DecodedToken>(token);
  return {
    fullName: decoded.fullName,
    userId: decoded.userId,
    userCode: decoded.userCode,
    rolesNames: decoded.rolesName,
    rolesCodes: decoded.rolesCode,
    emailFpt: decoded.emailFpt,
    pictureUrl: decoded.pictureUrl,
    facilityCode: decoded.facilityCode,
    facilityId: decoded.facilityId,
    facilityName: decoded.facilityName,
    departmentCode: decoded.departmentCode,
    departmentName: decoded.departmentName,
    emailFe: decoded.emailFe,
    semesterId: decoded.semesterId,
    blockId: decoded.blockId,
  };
};

export const getRolesUser = (token: string): string[] => {
  const decoded = jwtDecode<DecodedToken>(token);
  return decoded.rolesCode;
};

export const getExpireTime = (token: string): Dayjs => {
  const decoded = jwtDecode<DecodedToken>(token);
  return dayjs(decoded.exp);
};

export const isTokenExpired = (token: string): boolean => {
  const expireTime = getExpireTime(token);
  return dayjs().isAfter(expireTime);
};
