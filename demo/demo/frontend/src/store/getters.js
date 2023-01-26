import receives from "./getters/receives";
import users from "./getters/users";

export default {
  getHeaderDarkMode(state) {
    return state.headerDarkNode;
  },
 

  ...users,
  ...receives,
};
