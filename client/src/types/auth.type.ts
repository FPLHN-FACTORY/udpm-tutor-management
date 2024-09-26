export interface DecodedToken {
  fullName: string;
  userId: string;
  userCode: string;
  rolesName: string;
  rolesCode: string[];
  exp: number;
  emailFpt: string;
  emailFe: string;
  pictureUrl: string;
  facilityCode: string;
  facilityId: string;
  facilityName: string;
  departmentName: string;
  departmentCode: string;
  semesterId: string;
  blockId: string;
}

export interface UserInformation {
  fullName: string;
  userId: string;
  userCode: string;
  rolesNames: string[] | string;
  rolesCodes: string[] | string;
  emailFpt: string;
  emailFe: string;
  pictureUrl: string;
  facilityCode: string;
  facilityId: string;
  facilityName: string;
  departmentName: string;
  departmentCode: string;
  semesterId: string;
  blockId: string;
}
