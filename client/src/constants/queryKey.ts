export const queryKey = {
  common: {
    departmentOptions: "departmentOptionsKey",
    facilityOptions: "facilityOptionsKey",
    semesterOptions: "semesterOptionsKey",
    blockOptions: "blockOptionsKey",
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
      departmentSynchronize: "departmentSynchronizeKey",
    },
    major: {
      majorList: "majorListKey",
      majorDetail: "majorDetailKey",
      majorSynchronize: "majorSynchronizeKey",
    },
    majorFacility: {
      majorFacilityList: "majorFacilityListKey",
      majorFacilitySynchronize: "majorFacilitySynchronizeKey",
    },
    departmentFacility: {
      departmentFacilityList: "departmentFacilityListKey",
      departmentFacilityDetail: "departmentFacilityDetailKey",
      departmentFacilitySynchronize: "departmentFacilitySynchronizeKey",
    },
    facility: {
      facilityList: "facilityListKey",
      facilityDetail: "facilityDetailKey",
      facilitySynchronize: "facilitySynchronizeKey"
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
  },
  planner: {
    plan: {
      planList: "planListKey",
      tutorClassList: "tutorClassListKey",
      semesterInfo: "semesterInfo",
      planInfo: "planInfo",
      planDetail: "planDetailKey",
    },
  }
};
