package socialnetwork.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import socialnetwork.domain.*;
import socialnetwork.domain.exceptions.RepoException;
import socialnetwork.service.NetworkService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

public class UserController extends AbstractController {

    ObservableList<UserDTO> modelGradeUserDTO = FXCollections.observableArrayList();
    ObservableList<FriendshipDTO> modelGradeFriendshipDTO = FXCollections.observableArrayList();
    ObservableList<FriendshipDTO> modelGradeFriendshipDTORequests = FXCollections.observableArrayList();
    ObservableList<MessageDTO> modelGradeMessages = FXCollections.observableArrayList();
    @FXML
    TableView<FriendshipDTO> tableMyFriends;
    @FXML
    TableColumn<FriendshipDTO, String> friendWith, friendFrom;
    /*----------------------------------------*/
    @FXML
    TableView<UserDTO> tableAllUsers;
    @FXML
    TableColumn<UserDTO, String> userName;
    /*----------------------------------------*/
    @FXML
    TableView<FriendshipDTO> tableFriendRequests;
    @FXML
    TableColumn<FriendshipDTO, String> sendByMe, receivedByMe;
    /*----------------------------------------*/
    @FXML
    TableView<MessageDTO> tableMessages;
    @FXML
    TableColumn<MessageDTO, String> sent, received;
    /*----------------------------------------*/
    @FXML
    ComboBox<String> comboBoxOptions, userComboBox;
    /*----------------------------------------*/
    @FXML
    Button deleteFriendButton, addFriendButton, acceptFriendshipButton,
            refuseFriendshipButton, signOutButton, deleteAccountButton,
            refreshButton, cancelFriendshipButton, sendMessageButton;
    /*----------------------------------------*/
    @FXML
    Text showSelectedUser, showResult, showSelectedUserByMe, selectedUserByMeText;
    @FXML
    TextField messageField;
    @FXML
    AnchorPane anchorRequests, anchorMessages;

    private <E> void setWrappingTextForColumns(TableColumn<E, String> t) {
        t.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    Text text = new Text(item);
                    text.setStyle("-fx-text-alignment:justify;");
                    text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(35));
                    setGraphic(text);
                }
            }
        });
    }

    @FXML
    public void initialize() {
        tableMyFriends.setVisible(false);
        tableAllUsers.setVisible(false);
        tableFriendRequests.setVisible(false);

        anchorRequests.setVisible(false);
        anchorMessages.setVisible(false);

        friendFrom.setCellValueFactory(new PropertyValueFactory<>("friendFrom"));
        friendWith.setCellValueFactory(new PropertyValueFactory<>("friendWith"));

        userName.setCellValueFactory(new PropertyValueFactory<>("name"));

        sendByMe.setCellValueFactory(new PropertyValueFactory<>("sendByMe"));
        receivedByMe.setCellValueFactory(new PropertyValueFactory<>("receivedByMe"));

        sent.setCellValueFactory(new PropertyValueFactory<>("messageSent"));
        received.setCellValueFactory(new PropertyValueFactory<>("messageReceived"));
        setWrappingTextForColumns(sent);
        setWrappingTextForColumns(received);

        tableMyFriends.setItems(modelGradeFriendshipDTO);
        tableAllUsers.setItems(modelGradeUserDTO);
        tableFriendRequests.setItems(modelGradeFriendshipDTORequests);
        tableMessages.setItems(modelGradeMessages);
        comboBoxOptions.getSelectionModel().selectedItemProperty().addListener(
                (x, y, z) -> handleFilter()
        );
        userComboBox.getSelectionModel().selectedItemProperty().addListener(
                (x, y, z) -> handleFilterMessages()
        );
    }

    public void setService(NetworkService service, User user) {
        super.init(service, user);
        modelGradeUserDTO.setAll(getAllUsersList());
        modelGradeFriendshipDTO.setAll(getAllFriendshipsForThisUser());
        modelGradeFriendshipDTORequests.setAll(getAllFriendshipRequests());
        String friend = userComboBox.getSelectionModel().getSelectedItem();
        if (friend != null)
            modelGradeMessages.setAll(getAllMessagesForThisUserWithParticularFriend(friend));
        else
            modelGradeMessages.setAll((MessageDTO) null);
        comboBoxOptions.getItems().setAll("See my friends", "See all users", "See friend requests", "Messenger");
        userComboBox.getItems().clear();
        for (FriendshipDTO friendshipDTO : getAllFriendshipsForThisUser()) {
            String name = friendshipDTO.getFriendWith();
            if (!userComboBox.getItems().contains(name))
                userComboBox.getItems().add(name);
        }
    }

    private void handleFilterMessages() {
        String selectedItem = userComboBox.getSelectionModel().getSelectedItem();
        if (selectedItem != null)
            modelGradeMessages.setAll(getAllMessagesForThisUserWithParticularFriend(selectedItem));
    }

    private void handleFilter() {
        String selectedItem = comboBoxOptions.getSelectionModel().getSelectedItem();
        switch (selectedItem) {
            case "See my friends" -> {
                tableMyFriends.setVisible(true);
                tableFriendRequests.setVisible(false);
                tableAllUsers.setVisible(false);
                anchorRequests.setVisible(false);
                anchorMessages.setVisible(false);
            }
            case "See all users" -> {
                tableMyFriends.setVisible(false);
                tableFriendRequests.setVisible(false);
                tableAllUsers.setVisible(true);
                anchorRequests.setVisible(false);
                anchorMessages.setVisible(false);
            }
            case "See friend requests" -> {
                tableMyFriends.setVisible(false);
                tableFriendRequests.setVisible(true);
                tableAllUsers.setVisible(false);
                anchorRequests.setVisible(true);
                anchorMessages.setVisible(false);
            }
            case "Messenger" -> {
                tableMyFriends.setVisible(false);
                tableFriendRequests.setVisible(false);
                tableAllUsers.setVisible(false);
                anchorRequests.setVisible(false);
                anchorMessages.setVisible(true);
            }
        }
    }

    private List<FriendshipDTO> getAllFriendshipsForThisUser() {
        Iterable<Friendship> list = service.getAllFriendships();
        List<FriendshipDTO> friendshipDTOList = StreamSupport.stream(list.spliterator(), false)
                .filter((x) -> (Objects.equals(x.getFirstFriendId(), currentUser.getId())
                        && Objects.equals(x.getStatus(), "accepted")))
                .map(f -> new FriendshipDTO(f.getDate().toString(),
                        f.getSecondFriend().getLastName() + " " + f.getSecondFriend().getFirstName(), null, null)).toList();
        List<FriendshipDTO> friendshipDTOList2 = StreamSupport.stream(list.spliterator(), false)
                .filter((x) -> (Objects.equals(x.getSecondFriendId(), currentUser.getId())
                        && Objects.equals(x.getStatus(), "accepted")))
                .map(f -> new FriendshipDTO(f.getDate().toString(),
                        f.getFirstFriend().getLastName() + " " + f.getFirstFriend().getFirstName(), null, null)).toList();
        List<FriendshipDTO> friendshipDTOListCombined = new ArrayList<>();
        friendshipDTOListCombined.addAll(friendshipDTOList);
        friendshipDTOListCombined.addAll(friendshipDTOList2);
        return friendshipDTOListCombined;
    }

    private List<MessageDTO> getAllMessagesForThisUserWithParticularFriend(String friend) {
        if (friend != null) {
            Iterable<Message> list = service.getAllMessages();
            User userFriend = service.findByName(friend);
            List<String> sent = StreamSupport.stream(list.spliterator(), false)
                    .filter((x) -> (Objects.equals(x.getFrom(), currentUser.getId().intValue())
                            && x.getTo() == userFriend.getId().intValue()))
                    .map(f -> f.getId().toString() + ";;" + f.getMessage()).toList();
            List<String> received = StreamSupport.stream(list.spliterator(), false)
                    .filter((x) -> (Objects.equals(x.getTo(), currentUser.getId().intValue())
                            && x.getFrom() == userFriend.getId().intValue()))
                    .map(f -> f.getId().toString() + ";;" + f.getMessage()).toList();
            List<MessageDTO> messages = new ArrayList<>();
            int i1 = 0, i2 = 0;
            int id1, id2;
            String[] idPlusMessageI1, idPlusMessageI2;
            while (i1 < sent.size() && i2 < received.size()) {
                idPlusMessageI1 = sent.get(i1).split(";;", 2);
                id1 = Integer.parseInt(idPlusMessageI1[0]);
                idPlusMessageI2 = received.get(i2).split(";;", 2);
                id2 = Integer.parseInt(idPlusMessageI2[0]);
                if (id1 < id2) {
                    messages.add(new MessageDTO(idPlusMessageI1[1], ""));
                    i1++;
                } else {
                    messages.add(new MessageDTO("", idPlusMessageI2[1]));
                    i2++;
                }
            }
            while (i1 < sent.size()) {
                idPlusMessageI1 = sent.get(i1).split(";;", 2);
                messages.add(new MessageDTO(idPlusMessageI1[1], ""));
                i1++;
            }
            while (i2 < received.size()) {
                idPlusMessageI2 = received.get(i2).split(";;", 2);
                messages.add(new MessageDTO("", idPlusMessageI2[1]));
                i2++;
            }
            return messages;
        }
        return null;
    }


    @FXML
    public void addFriend() {
        if (Objects.equals(showSelectedUser.getText(), "")) {
            showResult.setText("You haven't selected any user yet!");
        } else {
            User toBeFriendWith = service.findByName(showSelectedUser.getText());
            Long id = toBeFriendWith.getId();
            try {
                service.addFriendship(currentUser.getId(), id, false);
                showResult.setText("Friendship with " + showSelectedUser.getText() + " added successfully!");
            } catch (RepoException e) {
                showResult.setText(e.toString());
            }
        }
    }

    @FXML
    public void deleteFriend() {
        if (Objects.equals(showSelectedUser.getText(), "")) {
            showResult.setText("You haven't selected any user yet!");
        } else {
            User toDeleteFriendship = service.findByName(showSelectedUser.getText());
            Long id = toDeleteFriendship.getId();
            try {
                userComboBox.getItems().clear();
                service.deleteFriendship(currentUser.getId(), id);
                showResult.setText("Friendship with " + showSelectedUser.getText() + " deleted successfully!");
            } catch (RepoException e) {
                showResult.setText(e.toString());
            }
        }
    }


    @FXML
    public void showUserTableAllUsers() {
        UserDTO user = tableAllUsers.getSelectionModel().getSelectedItem();
        if(user!=null)
            showSelectedUser.setText(user.getName());
    }

    @FXML
    public void showUserTableFriendRequests() {
        FriendshipDTO user = tableFriendRequests.getSelectionModel().getSelectedItem();
        if (user != null) {
            String longFormReceivedByMe = user.getReceivedByMe();
            String longFormSendByMe = user.getSendByMe();
            try {
                String[] partsReceivedByMe = longFormReceivedByMe.split(";", 2);
                showSelectedUser.setText(partsReceivedByMe[0]);
                String[] partsSendByMe = longFormSendByMe.split(";", 2);
                showSelectedUserByMe.setText(partsSendByMe[0]);
            } catch (Exception ignored) {
            }
        }
    }

    @FXML
    public void showUserTableMyFriends() {
        FriendshipDTO user = tableMyFriends.getSelectionModel().getSelectedItem();
        if (user != null)
            showSelectedUser.setText(user.getFriendWith());
    }

    @FXML
    public void acceptFriendshipAction() {
        String selectedUser = showSelectedUser.getText();
        if (selectedUser.isEmpty())
            showResult.setText("Nothing selected!");
        else {
            try {
                service.acceptFriendship(currentUser, service.findByName(selectedUser));
                showResult.setText("Friendship with " + selectedUser + " accepted successfully!");
            } catch (RepoException e) {
                showResult.setText(e.toString());
            }
        }
    }

    @FXML
    public void refuseFriendshipAction() {
        String selectedUser = showSelectedUser.getText();
        if (selectedUser.isEmpty())
            showResult.setText("Nothing selected!");
        else {
            try {
                service.refuseFriendship(currentUser, service.findByName(selectedUser));
                showResult.setText("Friendship with " + selectedUser + " rejected successfully!");
            } catch (RepoException e) {
                showResult.setText(e.toString());
            }
        }
    }

    @FXML
    public void cancelFriendshipAction() {
        String selectedUser = showSelectedUserByMe.getText();
        if (selectedUser.isEmpty())
            showResult.setText("Nothing selected!");
        else {
            try {
                service.refuseFriendship(service.findByName(selectedUser), currentUser);
                showResult.setText("Friendship with " + selectedUser + " canceled successfully!");
            } catch (RepoException e) {
                showResult.setText(e.toString());
            }
        }
    }

    @FXML
    public void sendMessageAction() {
        String message = messageField.getText();
        messageField.clear();
        if (message.isEmpty())
            showResult.setText("The message is empty and you can't send an empty message!");
        else {
            String selectedFriend = userComboBox.getSelectionModel().getSelectedItem();
            if (selectedFriend == null)
                showResult.setText("You haven't chosen a friend to send the message!");
            else
                service.sendMessage(currentUser.getId(), service.findByName(selectedFriend).getId(), message);
        }
    }

    @FXML
    public void signOutAction() throws IOException {
        Stage previousStage = (Stage) signOutButton.getScene().getWindow();
        super.signOutAction(previousStage);
    }

    @FXML
    public void deleteAccountAction() {
        try {
            service.deleteUser(currentUser.getId());
            signOutAction();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void refresh() {
        service.refreshService();
        update();
    }

    @Override
    public void update() {
        modelGradeUserDTO.setAll(getAllUsersList());
        modelGradeFriendshipDTO.setAll(getAllFriendshipsForThisUser());
        modelGradeFriendshipDTORequests.setAll(getAllFriendshipRequests());
        String friend = userComboBox.getSelectionModel().getSelectedItem();
        if (friend != null)
            modelGradeMessages.setAll(getAllMessagesForThisUserWithParticularFriend(friend));
        else
            modelGradeMessages.setAll((MessageDTO) null);
        currentUser = service.findOne(currentUser.getId());

        for (FriendshipDTO friendshipDTO : getAllFriendshipsForThisUser()) {
            String name = friendshipDTO.getFriendWith();
            if (!userComboBox.getItems().contains(name))
                userComboBox.getItems().add(name);
        }
    }
}
