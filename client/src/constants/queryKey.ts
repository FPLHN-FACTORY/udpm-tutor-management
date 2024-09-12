import {getStaffDepartmentMajorStaff} from "@/services/api/staff.api.ts";

export const queryKey = {
  common: {
    departmentOptions: "departmentOptionsKey",
    facilityOptions: "facilityOptionsKey",
  },
  admin: {
    subject: {
      subjectList: "subjectListKey",
      subjectDetail: "subjectDetailKey",
    },
    role: {
      roleList: "roleListKey",
      roleDetail: "roleDetailKey",
    },
    department: {
      departmentList: "departmentListKey",
      departmentDetail: "departmentDetailKey",
    },
    departmentFacility: {
      departmentFacilityList: "departmentFacilityListKey",
      departmentFacilityDetail: "departmentFacilityDetailKey",
    },
    facility: {
      facilityList: "facilityListKey",
      facilityDetail: "facilityDetailKey",
    },
    facilityChild: {
      facilityChildList: "facilityChildListKey",
      facilityChildDetail: "facilityChildDetailKey",
    },
    semester: {
      semesterList: "semesterListKey",
      semesterDetail: "semesterDetailKey",
    },
    block: {
      blockList: "blockListKey",
      blockDetail: "blockDetailKey",
    },
    staff: {
      staffList: "staffListKey",
      staffDetail: "staffDetailKey",
      staffRole: "staffRole",
      staffDepartmentMajor: "staffDepartmentMajor",
    },
  },
};
