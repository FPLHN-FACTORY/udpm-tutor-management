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
      subjectManagedByStaff: "subjectManagedByStaffKey",
    },
    role: {
      roleList: "roleListKey",
      roleDetail: "roleDetailKey",
    },
    department: {
      departmentList: "departmentListKey",
      departmentDetail: "departmentDetailKey",
      departmentSynchronize: "departmentSynchronizeKey",
      departmentManagedByStaff: "departmentManagedByStaffKey",
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
    headOfPlan: {
      headOfPlanList: "headOfPlanListKey",
      planByHeadOfPlan: " planByHeadOfPlanKey",
      staffByHeadOfPlan: " staffByHeadOfPlanKey",
      headOfPlanDetail: "headOfPlanDetailKey",
    },
  },
  planner: {
    plan: {
      planList: "pLPlPlanListKey",
      semesterInfo: "pLPlSemesterInfo",
      planInfo: "pLPlPlanInfo",
      planDetail: "pLPlPlanDetailKey",
      tutorClassList: "pLPlTutorClassListKey",
      tutorDetail: "pLPlTutorDetailKey",
      tutorList: "pLPlTutorListKey"
    },
  },
  headSubject: {
    plan: {
      planList: "hSPlPlanListKey",
      semesterInfo: "hSPlSemesterInfo",
      planInfo: "hSPlPlanInfo",
      planDetail: "hSPlPlanDetailKey",
      tutorClassList: "hSPlTutorClassListKey",
      tutorDetailList: "hSPlTutorDetailListKey",
      tutorDetail: "hSPlTutorDetailKey",
      tutorList: "hSPlTutorListKey"
    },
  }
};
