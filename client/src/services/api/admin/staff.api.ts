import {DefaultResponse, PaginationParams, PaginationResponse, ResponseList} from "@/types/api.common.ts";
import {PREFIX_API_STAFF_ADMIN} from "@/constants/url.ts";
import {Ref} from "vue";
import request from "@/services/request.ts";
import {AxiosResponse} from "axios";

export interface ParamsGetStaff extends PaginationParams {
    searchQuery?: string | null;
}

export type StaffResponse = ResponseList & {
    staffName: string;
    staffCode: string;
    emailFe: string;
    emailFpt: string;
    createdDate: number;
};

export type DetailStaffResponse = {
    id: string;
    staffName: string;
    staffCode: string;
    emailFe: string;
    emailFpt: string;
};

export type RoleResponse = {
    roleName: string;
    roleCode: string;
    roleId: string;
    facilityName: string;
    orderNumber: number;
};

export type DepartmentMajorResponse = {
    departmentName: string;
    majorName: string;
    facilityName: string;
    id: string;
    orderNumber: number;
};

export type DepartmentMajorDetailResponse = {
    staffMajorFacilityId: string;
    facilityId: string;
    departmentId: string;
    majorId: string;
};

export const getStaffs = async (params: Ref<ParamsGetStaff>) => {
    const res = (await request({
        url: `${PREFIX_API_STAFF_ADMIN}`,
        method: "GET",
        params: params.value,
    })) as AxiosResponse<
        DefaultResponse<PaginationResponse<Array<StaffResponse>>>
    >;
    return res.data;
};

export const getDetailStaff = async (id: string[] | string | null) => {
    const res = (await request({
        url: `${PREFIX_API_STAFF_ADMIN}/${id}`,
        method: "GET",
    })) as AxiosResponse<DefaultResponse<DetailStaffResponse>>;

    return res.data;
};

export const getStaffRoles = async (id: string | null) => {
    const res = (await request({
        url: `${PREFIX_API_STAFF_ADMIN}/role/${id}`,
        method: "GET",
    })) as AxiosResponse<DefaultResponse<Array<RoleResponse>>>;

    return res.data;
};

export const getStaffDepartmentMajor = async (id: string | null) => {
    const res = (await request({
        url: `${PREFIX_API_STAFF_ADMIN}/major-facility/${id}`,
        method: "GET",
    })) as AxiosResponse<DefaultResponse<Array<DepartmentMajorResponse>>>;

    return res.data;
};

export const getStaffSynchronize = async (campusCode: string | null) => {
    const res: AxiosResponse<DefaultResponse<string>> = await request({
        url: `${PREFIX_API_STAFF_ADMIN}/synchronize`,
        method: 'GET',
        params: { campusCode },
    });

    return res.data;
};

export interface CreateUpdateStaffParams {
    name: string;
    staffCode: string;
    emailFe: string;
    emailFpt: string;
    id?: string
}

export const createStaff = async (params: CreateUpdateStaffParams) => {
    const res = (await request({
        url: `${PREFIX_API_STAFF_ADMIN}`,
        method: "POST",
        data: params,
    })) as AxiosResponse<DefaultResponse<null>>;

    return res.data;
};

export const updateStaff = async (
    params: CreateUpdateStaffParams
) => {
    const res = (await request({
        url: `${PREFIX_API_STAFF_ADMIN}`,
        method: "PUT",
        data: params,
    })) as AxiosResponse<DefaultResponse<null>>;

    return res.data;
};

export const getRoleByStaff = async (staffId: string) => {
    const res = (await request({
        url: `${PREFIX_API_STAFF_ADMIN}/role/${staffId}`,
        method: "GET",
    })) as AxiosResponse<
        DefaultResponse<PaginationResponse<Array<StaffResponse>>>
    >;
    return res.data;
};

export interface UpdateStaffPermissionParams {
    idStaff: string,
    idRole: string
}

export const updateStaffPermission = async (
    params: UpdateStaffPermissionParams
) => {
    const res = (await request({
        url: `${PREFIX_API_STAFF_ADMIN}/change-permission`,
        method: "PUT",
        data: params,
    })) as AxiosResponse<DefaultResponse<null>>;

    return res.data;
};


export interface CreateUpdateStaffDepartmentMajorParams {
    idStaffMajorFacility?: string,
    idStaff: string | null
    idFacility: string
    idDepartment: string
    idMajor: string
}

export const createStaffDepartmentMajor = async (
    params: CreateUpdateStaffDepartmentMajorParams
) => {
    const res = (await request({
        url: `${PREFIX_API_STAFF_ADMIN}/staff-major-facility`,
        method: "POST",
        data: params,
    })) as AxiosResponse<DefaultResponse<null>>;

    return res.data;
};

export const updateStaffDepartmentMajor = async (
    params: CreateUpdateStaffDepartmentMajorParams
) => {
    const res = (await request({
        url: `${PREFIX_API_STAFF_ADMIN}/staff-major-facility`,
        method: "PUT",
        data: params,
    })) as AxiosResponse<DefaultResponse<null>>;

    return res.data;
};

export const getDetailStaffDepartmentMajor = async (idStaffMajorFacility: string | null) => {
    const res = (await request({
        url: `${PREFIX_API_STAFF_ADMIN}/staff-major-facility`,
        method: "GET",
        params: {
            idStaffMajorFacility: idStaffMajorFacility
        }
    })) as AxiosResponse<DefaultResponse<DepartmentMajorDetailResponse>>;

    return res.data;
};

