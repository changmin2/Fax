import receives from "./getters/receives";
import sends from "./getters/sends";
import users from "./getters/users";

export default {
  getHeaderDarkMode(state) {
    return state.headerDarkNode;
  },

  getIsLoading(state) {
    return state.getIsLoading;
  },

  ...users,
  ...receives,
  ...sends,
};
