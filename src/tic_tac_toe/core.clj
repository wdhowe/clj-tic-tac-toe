(ns tic-tac-toe.core
  (:gen-class))

(defn starting-board
  "Returns a map of the starting game board."
  []
  (let [blank "-"]
    {:blank blank
     :row1 {:col1 blank :col2 blank :col3 blank}
     :row2 {:col1 blank :col2 blank :col3 blank}
     :row3 {:col1 blank :col2 blank :col3 blank}}))

(defn starting-state
  "Returns a map of the starting game state."
  []
  (let [player1-mark "x"
        player2-mark "o"]
    {:board (starting-board)
     :player-turn player1-mark
     :player1 player1-mark
     :player2 player2-mark}))

(defn show-board
  "Display the game board."
  [board]
  (let [sep "|"
        line "---"
        header (str "  1   2   3 (col)\n" sep line sep line sep line sep " (row)")
        footer (str sep line sep line sep line sep)]
    (println header)

    (print sep)
    (doseq [mark (vals (:row1 board))] (print "" mark sep))
    (print " 1\n")

    (print sep)
    (doseq [mark (vals (:row2 board))] (print "" mark sep))
    (print " 2\n")

    (print sep)
    (doseq [mark (vals (:row3 board))] (print "" mark sep))
    (print " 3\n")

    (println footer)))

(defn valid-move?
  "Ensure a valid row,col is given for the move."
  [state row col]
  (let [row-int (Integer/parseInt row)
        col-int (Integer/parseInt col)
        blank (get-in state [:board :blank])]
    (if (and (<= 1 row-int 3)
             (<= 1 col-int 3)
             (= blank
                (get-in state
                        [:board
                         (keyword (str "row" row-int))
                         (keyword (str "col" col-int))])))
      true ; row/col is 1-3 and currently blank
      false)))

(defn player-input
  "Prompt and validate player input for the move. Return a map of the row/col move."
  [state]
  (loop []
    (let [col (do (print "Enter col:")
                  (flush)
                  (read-line))
          row (do (print "Enter row:")
                  (flush)
                  (read-line))]
      (if (valid-move? state row col)
        {:row row :col col}
        (do
          (show-board (:board state))
          (println "-> Invalid row/col or non-empty space. Enter 1,2, or 3 for each.")
          (println "-> Player turn:" (:player-turn state))
          (recur))))))

(defn mark-move
  "Mark the player's move on the board."
  [state move]
  (assoc-in state [:board
                   (keyword (str "row" (:row move)))
                   (keyword (str "col" (:col move)))]
            (:player-turn state)))

(defn take-turn
  "Get the player's move and mark it on the board."
  [state]
  (println "-> Player turn:" (:player-turn state))
  (let [player-move (player-input state)]
    (mark-move state player-move)))

(defn three-marks
  "Check for player marks. If there is three for either player mark, return the mark.
   If there is not three of either mark, return nil."
  [state board-vals]
  (let [player1 (:player1 state)
        player2 (:player2 state)
        player1-only (filter #(= player1 %) board-vals)
        player2-only (filter #(= player2 %) board-vals)]
    (cond
      (= 3 (count player1-only)) player1
      (= 3 (count player2-only)) player2
      :else nil)))

(defn check-rows
  "Check all rows for player marks. Return a map of each row and the result."
  [state]
  (let [row1 (three-marks state (vals (get-in state [:board :row1])))
        row2 (three-marks state (vals (get-in state [:board :row2])))
        row3 (three-marks state (vals (get-in state [:board :row3])))]
    {:row1 row1
     :row2 row2
     :row3 row3}))

(defn col-vals
  "Returns a vector of column values."
  [board col]
  (let [kw-col (keyword col)]
    [(get-in board [:row1 kw-col])
     (get-in board [:row2 kw-col])
     (get-in board [:row3 kw-col])]))

(defn check-cols
  "Check all columns for player marks. Return a map of each column and the result."
  [state]
  (let [col1 (three-marks state (col-vals (:board state) "col1"))
        col2 (three-marks state (col-vals (:board state) "col2"))
        col3 (three-marks state (col-vals (:board state) "col3"))]
    {:col1 col1
     :col2 col2
     :col3 col3}))

(defn diag-vals
  "Returns a vector of values from diag left or right."
  [board diag-dir]
  (case diag-dir
    "right" [(get-in board [:row1 :col1])
             (get-in board [:row2 :col2])
             (get-in board [:row3 :col3])]
    "left" [(get-in board [:row1 :col3])
            (get-in board [:row2 :col2])
            (get-in board [:row3 :col1])]
    []))

(defn check-diags
  "Check diagonals for player marks. Return a map of each diagonal and the result."
  [state]
  (let [diag-right (three-marks state (diag-vals (:board state) "right"))
        diag-left (three-marks state (diag-vals (:board state) "left"))]
    {:diag-right diag-right
     :diag-left diag-left}))

(defn find-winner
  "Check a map of moves for the winner."
  [state moves]
  (let [player1 (:player1 state)
        player2 (:player2 state)]
    (into {} (filter (fn [[k v]] (or (= player1 v) (= player2 v))) moves))))

(defn check-winner
  "Check the board for a winner.
   Return a map of the win condition(true/false), the player that won, and the winning move."
  [state]
  (let [row-results (check-rows state)
        col-results (check-cols state)
        diag-results (check-diags state)
        all-results (merge row-results col-results diag-results)
        winning-move (find-winner state all-results)]
    (if-not (empty? winning-move)
      {:win true
       :player (first (vals winning-move))
       :move (first (keys winning-move))}
      {:win false})))

(defn next-turn
  "Change the turn to the next player."
  [state]
  (if (= (:player-turn state) (:player1 state))
    (assoc state :player-turn (:player2 state))
    (assoc state :player-turn (:player1 state))))

(defn moves-left?
  "Check the board to see if there are any available spaces left."
  [state]
  (let [blank (get-in state [:board :blank])
        all-vals (flatten (conj (vals (get-in state [:board :row1]))
                                (vals (get-in state [:board :row2]))
                                (vals (get-in state [:board :row3]))))]
    (if (some #{blank} all-vals)
      true
      false)))

(defn tie-game
  "Returns the data structure for a tie/cats game."
  []
  {:win false
   :player "nobody"
   :move "tie/cats game"})

(defn end-game
  "Display the end game results."
  [state winner]
  (println (show-board (:board state)))
  (println "And the Winner is:" (:player winner))
  (println "Winning move was on:" (symbol (:move winner))))

(defn -main
  "Tic-Tac-Toe Game."
  []
  (println "Welcome to Tic-Tac-Toe!")
  (loop [state (starting-state)]
    (show-board (:board state))
    (let [new-state (take-turn state)
          winner-results (check-winner new-state)]
      (if (:win winner-results)
        (end-game new-state winner-results)
        (if-not (moves-left? new-state)
          (end-game new-state (tie-game))
          (recur (next-turn new-state)))))))

;; comment forms are useful for running REPL connected tests
;; while developing. unit tests can very easily be created from these as well.
(comment
  (def test-state {:board
                   {:blank "-"
                    :row1 {:col1 "-", :col2 "-", :col3 "-"}
                    :row2 {:col1 "-", :col2 "-", :col3 "-"}
                    :row3 {:col1 "-", :col2 "-", :col3 "-"}}
                   :player-turn "x"
                   :player1 "x"
                   :player2 "o"})

  (starting-board)
  (starting-state)
  (show-board (:board test-state))

  (valid-move? test-state "5" "1") ; invalid move
  (valid-move? test-state "1" "1") ; valid move
  (valid-move? test-state "3" "3") ; valid move

  (mark-move test-state {:row 1 :col 1})
  (mark-move test-state {:row 3 :col 3})

  ; three in a row checks
  (three-marks test-state '("x" "x" "x")) ; yes
  (three-marks test-state '("o" "o" "o")) ; yes
  (three-marks test-state '("x" "-" "x")) ; no
  (three-marks test-state '("-" "-" "-")) ; no
  (three-marks test-state '("x" "x" "o")) ; no

  (check-rows {:board
               {:blank "-"
                :row1 {:col1 "-", :col2 "-", :col3 "-"}
                :row2 {:col1 "x", :col2 "x", :col3 "x"}
                :row3 {:col1 "-", :col2 "-", :col3 "-"}}
               :player-turn "x"
               :player1 "x"
               :player2 "o"})
  (check-rows {:board
               {:blank "-"
                :row1 {:col1 "o", :col2 "x", :col3 "-"}
                :row2 {:col1 "x", :col2 "o", :col3 "x"}
                :row3 {:col1 "-", :col2 "o", :col3 "-"}}
               :player-turn "x"
               :player1 "x"
               :player2 "o"})

  (col-vals {:blank "-"
             :row1 {:col1 "o", :col2 "o", :col3 "-"}
             :row2 {:col1 "x", :col2 "o", :col3 "x"}
             :row3 {:col1 "x", :col2 "o", :col3 "-"}} "col1")

  (check-cols {:board
               {:blank "-"
                :row1 {:col1 "o", :col2 "o", :col3 "-"}
                :row2 {:col1 "x", :col2 "o", :col3 "x"}
                :row3 {:col1 "x", :col2 "o", :col3 "-"}}
               :player-turn "x"
               :player1 "x"
               :player2 "o"})

  (diag-vals {:blank "-"
              :row1 {:col1 "o", :col2 "o", :col3 "x"}
              :row2 {:col1 "x", :col2 "o", :col3 "x"}
              :row3 {:col1 "x", :col2 "x", :col3 "-"}} "right")
  (diag-vals {:blank "-"
              :row1 {:col1 "o", :col2 "o", :col3 "x"}
              :row2 {:col1 "x", :col2 "o", :col3 "x"}
              :row3 {:col1 "x", :col2 "x", :col3 "-"}} "left")

  (check-diags {:board
                {:blank "-"
                 :row1 {:col1 "o", :col2 "o", :col3 "-"}
                 :row2 {:col1 "x", :col2 "o", :col3 "x"}
                 :row3 {:col1 "x", :col2 "x", :col3 "o"}}
                :player-turn "x"
                :player1 "x"
                :player2 "o"})

  (find-winner test-state {:col2 nil, :row2 nil
                           :diag-left "o", :row3 nil
                           :col3 nil, :diag-right nil
                           :row1 nil, :col1 nil})
  (find-winner test-state {:col2 nil, :row2 nil
                           :diag-left nil, :row3 nil
                           :col3 nil, :diag-right nil
                           :row1 nil, :col1 nil})

  (check-winner {:board
                 {:blank "-"
                  :row1 {:col1 "o", :col2 "o", :col3 "-"}
                  :row2 {:col1 "x", :col2 "o", :col3 "x"}
                  :row3 {:col1 "x", :col2 "x", :col3 "o"}}
                 :player-turn "x"
                 :player1 "x"
                 :player2 "o"})
  (check-winner {:board
                 {:blank "-"
                  :row1 {:col1 "o", :col2 "x", :col3 "x"}
                  :row2 {:col1 "x", :col2 "o", :col3 "o"}
                  :row3 {:col1 "o", :col2 "x", :col3 "x"}}
                 :player-turn "x"
                 :player1 "x"
                 :player2 "o"})

  (next-turn {:player-turn "x"
              :player1 "x"
              :player2 "o"})
  (next-turn {:player-turn "o"
              :player1 "x"
              :player2 "o"})

  ; places to move available
  (moves-left? {:board {:row1 {:col1 "-" :col2 "-" :col3 "-"}
                        :row2 {:col1 "-" :col2 "x" :col3 "-"}
                        :row3 {:col1 "o" :col2 "-" :col3 "-"}
                        :blank "-"}})
  ; no places to move available
  (moves-left? {:board {:row1 {:col1 "x" :col2 "x" :col3 "o"}
                        :row2 {:col1 "x" :col2 "o" :col3 "o"}
                        :row3 {:col1 "o" :col2 "x" :col3 "x"}
                        :blank "-"}})

  (tie-game)

  (end-game test-state {:winner true, :move :diag-left, :player "o"})
  (end-game test-state (tie-game)))