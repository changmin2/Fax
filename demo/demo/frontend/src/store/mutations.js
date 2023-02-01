import receives from "./mutations/receives";
import sends from "./mutations/sends";
import users from "./mutations/users";

export default {
  /* 헤더 다크모드 - 글씨, 버튼 색 반전 */
  SET_HEADER_LIGHT_MODE(state) {
    state.headerDarkMode = false;
  },
  SET_HEADER_DARK_MODE(state) {
    state.headerDarkMode = true;
  },
  SET_LOADING_FALSE(state) {
    state.isLoading = false;
  },
  SET_LOADING_TRUE(state) {
    state.isLoading = true;
  },
  SET_MODAL_OPEN(state) {
    state.isModalOpen = true;
  },
  SET_MODAL_CLOSE(state) {
    state.isModalOpen = false;
  },
  ...users,
  ...receives,
  ...sends,
};
