export const queryKey = {
  common: {
    departmentOptions: "departmentOptionsKey",
    facilityOptions: "facilityOptionsKey",
    semesterOptions: "semesterOptionsKey",
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
    major: {
      majorList: "majorListKey",
      majorDetail: "majorDetailKey",
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
      semesterDetail: "semesterDetailKey"
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
  headOfDepartment: {
    headOfSubject: {
      headOfSubjectList: "headOfSubjectListKey",
      subjectByHeadOfSubject: " subjectByHeadOfSubjectKey",
      staffByHeadOfSubject: " staffByHeadOfSubjectKey",
      headOfSubjectDetail: "headOfSubjectDetailKey",
    },
  }
};
