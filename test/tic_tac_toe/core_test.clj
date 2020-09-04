(ns tic-tac-toe.core-test
  (:require [clojure.test :refer :all]
            [tic-tac-toe.core :refer :all]))

(def test-state {:board
                 {:blank "-"
                  :row1 {:col1 "-", :col2 "-", :col3 "-"}
                  :row2 {:col1 "-", :col2 "-", :col3 "-"}
                  :row3 {:col1 "-", :col2 "-", :col3 "-"}}
                 :player-turn "x"
                 :player1 "x"
                 :player2 "o"})

(deftest test-starting-board
  (testing "Verify starting board map."
    (let [result (tic-tac-toe.core/starting-board)]
      (is (:blank result))
      (is (:row1 result))
      (is (:row2 result))
      (is (:row3 result)))))

(deftest test-starting-state
  (testing "Verify starting state."
    (let [result (tic-tac-toe.core/starting-state)]
      (is (:board result))
      (is (:player-turn result))
      (is (:player1 result))
      (is (:player2 result)))))

(deftest test-valid-move?
  (testing "Checking for valid moves."
    (are [func result] (= func result)
      (tic-tac-toe.core/valid-move? test-state "5" "1") false
      (tic-tac-toe.core/valid-move? test-state "1" "1") true
      (tic-tac-toe.core/valid-move? test-state "3" "3") true)))

(deftest test-mark-move
  (testing "Validate marking player moves on the board."
    (are [func result] (= func result)
      (get-in (tic-tac-toe.core/mark-move test-state {:row 1 :col 1})
              [:board :row1 :col1]) "x"
      (get-in (tic-tac-toe.core/mark-move test-state {:row 3 :col 3})
              [:board :row3 :col3]) "x")))

(deftest test-three-marks
  (testing "Check for three player marks."
    (are [func result] (= func result)
      (tic-tac-toe.core/three-marks test-state '("x" "x" "x")) "x"
      (tic-tac-toe.core/three-marks test-state '("o" "o" "o")) "o"
      (tic-tac-toe.core/three-marks test-state '("x" "-" "x")) nil
      (tic-tac-toe.core/three-marks test-state '("-" "-" "-")) nil
      (tic-tac-toe.core/three-marks test-state '("x" "x" "o")) nil)))

(deftest test-check-rows
  (testing "Check all rows for three in a row.")
  (are [func result] (= func result)
    (:row2 (tic-tac-toe.core/check-rows {:board
                                         {:blank "-"
                                          :row1 {:col1 "-", :col2 "-", :col3 "-"}
                                          :row2 {:col1 "x", :col2 "x", :col3 "x"}
                                          :row3 {:col1 "-", :col2 "-", :col3 "-"}}
                                         :player-turn "x"
                                         :player1 "x"
                                         :player2 "o"})) "x"
    (:row2 (tic-tac-toe.core/check-rows {:board
                                         {:blank "-"
                                          :row1 {:col1 "o", :col2 "x", :col3 "-"}
                                          :row2 {:col1 "x", :col2 "o", :col3 "x"}
                                          :row3 {:col1 "-", :col2 "o", :col3 "-"}}
                                         :player-turn "x"
                                         :player1 "x"
                                         :player2 "o"})) nil))

(deftest test-col-vals
  (testing "Checking column values."
    (are [func result] (= func result)
      (tic-tac-toe.core/col-vals {:blank "-"
                                  :row1 {:col1 "o", :col2 "o", :col3 "-"}
                                  :row2 {:col1 "x", :col2 "o", :col3 "x"}
                                  :row3 {:col1 "x", :col2 "o", :col3 "-"}} "col1") ["o" "x" "x"])))

(deftest test-check-cols
  (testing "Check all columns for three in a row."
    (are [func result] (= func result)
      (:col2 (tic-tac-toe.core/check-cols {:board
                                           {:blank "-"
                                            :row1 {:col1 "o", :col2 "o", :col3 "-"}
                                            :row2 {:col1 "x", :col2 "o", :col3 "x"}
                                            :row3 {:col1 "x", :col2 "o", :col3 "-"}}
                                           :player-turn "x"
                                           :player1 "x"
                                           :player2 "o"})) "o")))

(deftest test-diag-vals
  (testing "Checking diagonal values."
    (are [func result] (= func result)
      (tic-tac-toe.core/diag-vals {:blank "-"
                                   :row1 {:col1 "o", :col2 "o", :col3 "x"}
                                   :row2 {:col1 "x", :col2 "o", :col3 "x"}
                                   :row3 {:col1 "x", :col2 "x", :col3 "-"}} "right") ["o" "o" "-"]
      (tic-tac-toe.core/diag-vals {:blank "-"
                                   :row1 {:col1 "o", :col2 "o", :col3 "x"}
                                   :row2 {:col1 "x", :col2 "o", :col3 "x"}
                                   :row3 {:col1 "x", :col2 "x", :col3 "-"}} "left") ["x" "o" "x"])))

(deftest test-check-diags
  (testing "Check all diagonals for three in a row."
    (are [func result] (= func result)
      (:diag-right (tic-tac-toe.core/check-diags {:board
                                                  {:blank "-"
                                                   :row1 {:col1 "o", :col2 "o", :col3 "-"}
                                                   :row2 {:col1 "x", :col2 "o", :col3 "x"}
                                                   :row3 {:col1 "x", :col2 "x", :col3 "o"}}
                                                  :player-turn "x"
                                                  :player1 "x"
                                                  :player2 "o"})) "o")))

(deftest test-find-winner
  (testing "Checking maps of filtered results for winners."
    (are [func result] (= func result)
      (:diag-left (tic-tac-toe.core/find-winner test-state {:col2 nil, :row2 nil
                                                            :diag-left "o", :row3 nil
                                                            :col3 nil, :diag-right nil
                                                            :row1 nil, :col1 nil})) "o"
      (tic-tac-toe.core/find-winner test-state {:col2 nil, :row2 nil
                                                :diag-left nil, :row3 nil
                                                :col3 nil, :diag-right nil
                                                :row1 nil, :col1 nil}) {})))

(deftest test-check-winner
  (testing "Checking the board for a winner."
    (are [func result] (= func result)
      (:win (tic-tac-toe.core/check-winner {:board
                                            {:blank "-"
                                             :row1 {:col1 "o", :col2 "o", :col3 "-"}
                                             :row2 {:col1 "x", :col2 "o", :col3 "x"}
                                             :row3 {:col1 "x", :col2 "x", :col3 "o"}}
                                            :player-turn "x"
                                            :player1 "x"
                                            :player2 "o"})) true
      (:win (tic-tac-toe.core/check-winner {:board
                                            {:blank "-"
                                             :row1 {:col1 "o", :col2 "x", :col3 "x"}
                                             :row2 {:col1 "x", :col2 "o", :col3 "o"}
                                             :row3 {:col1 "o", :col2 "x", :col3 "x"}}
                                            :player-turn "x"
                                            :player1 "x"
                                            :player2 "o"})) false)))

(deftest test-next-turn
  (testing "Validate switching player turns."
    (are [func result] (= func result)
      (:player-turn (tic-tac-toe.core/next-turn {:player-turn "x"
                                                 :player1 "x"
                                                 :player2 "o"})) "o"
      (:player-turn (tic-tac-toe.core/next-turn {:player-turn "o"
                                                 :player1 "x"
                                                 :player2 "o"})) "x")))

(deftest test-moves-left?
  (testing "Checking for available spaces to move."
    (are [func result] (= func result)
      (tic-tac-toe.core/moves-left? {:board {:row1 {:col1 "-" :col2 "-" :col3 "-"}
                                             :row2 {:col1 "-" :col2 "x" :col3 "-"}
                                             :row3 {:col1 "o" :col2 "-" :col3 "-"}
                                             :blank "-"}}) true
      (tic-tac-toe.core/moves-left? {:board {:row1 {:col1 "x" :col2 "x" :col3 "o"}
                                             :row2 {:col1 "x" :col2 "o" :col3 "o"}
                                             :row3 {:col1 "o" :col2 "x" :col3 "x"}
                                             :blank "-"}}) false)))

(deftest test-tie-game
  (testing "Testing the tie-game structure."
    (let [result (tic-tac-toe.core/tie-game)]
      (is (= (:win result) false))
      (is (= (:player result) "nobody")))))
