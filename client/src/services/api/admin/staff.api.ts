import {DefaultResponse, PaginationParams, PaginationResponse, ResponseList} from "@/types/api.common.ts";
import {PREFIX_API_STAFF_ADMIN} from "@/constants/url.ts";
import {Ref} from "vue";
import request from "@/services/request.ts";
import {AxiosResponse} from "axios";
import {toast} from "vue3-toastify";
import {ERROR_MESSAGE} from "@/constants/message.constant.ts";

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
    staffMajorFacilityId: string;
    orderNumber: number;
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

export const getStaffSynchronize = async (campusCode: string) => {
    try {
        const res: AxiosResponse<DefaultResponse<string>> = await request({
            url: `${PREFIX_API_STAFF_ADMIN}/synchronize`,
            method: 'GET',
            params: { campusCode },
        });

        // Hiển thị thông báo thành công sau khi nhận được phản hồi
        toast.success("Đồng bộ nhân viên theo cơ sở thành công");
        return res.data;
    } catch (error) {
        // Hiển thị thông báo lỗi và ném lỗi để có thể được bắt trong handleSync
        toast.error(
            error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
        );
        throw error; // Ném lỗi để catch ở hàm gọi
    }
};


