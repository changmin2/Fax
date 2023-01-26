import receives from "./mutations/receives";
import users from "./mutations/users";

export default {
  /* 헤더 다크모드 - 글씨, 버튼 색 반전 */
  SET_HEADER_LIGHT_MODE(state) {
    state.headerDarkMode = false;
  },
  SET_HEADER_DARK_MODE(state) {
    state.headerDarkMode = true;
  },

  ...users,
  ...receives,
};
